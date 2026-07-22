package com.stockanalytics.provider.market;

import com.stockanalytics.config.MarketDataProviderProperties;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.exception.MarketDataUnavailableException;
import com.stockanalytics.exception.ProviderException;
import com.stockanalytics.provider.ProviderResult;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MarketDataProviderManagerTest {

    @Test
    void shouldUseFirstConfiguredProviderWhenItSucceeds() {
        MarketDataProvider firstProvider = mock(MarketDataProvider.class);
        MarketDataProvider secondProvider = mock(MarketDataProvider.class);

        when(firstProvider.getProviderCode()).thenReturn("provider-one");
        when(firstProvider.isEnabled()).thenReturn(true);

        when(secondProvider.getProviderCode()).thenReturn("provider-two");

        ProviderResult<List<InstrumentSearchResult>> expectedResult =
                successfulResult("provider-one", "Provider One");

        when(firstProvider.searchInstruments("RELIANCE"))
                .thenReturn(expectedResult);

        MarketDataProviderProperties properties =
                createProperties("provider-one", "provider-two");

        MarketDataProviderManager manager =
                new MarketDataProviderManager(
                        List.of(firstProvider, secondProvider),
                        properties
                );

        ProviderResult<List<InstrumentSearchResult>> result =
                manager.searchInstruments("RELIANCE");

        assertNotNull(result);
        assertSame(expectedResult, result);
        assertEquals("provider-one", result.getProviderCode());
        assertEquals("Provider One", result.getProviderDisplayName());

        verify(firstProvider).searchInstruments("RELIANCE");
        verify(secondProvider, never()).searchInstruments(anyString());
    }

    @Test
    void shouldUseSecondProviderWhenFirstProviderFails() {
        MarketDataProvider firstProvider = mock(MarketDataProvider.class);
        MarketDataProvider secondProvider = mock(MarketDataProvider.class);

        when(firstProvider.getProviderCode()).thenReturn("provider-one");
        when(firstProvider.isEnabled()).thenReturn(true);
        when(firstProvider.searchInstruments("RELIANCE"))
                .thenThrow(new ProviderException("Provider one failed"));

        when(secondProvider.getProviderCode()).thenReturn("provider-two");
        when(secondProvider.isEnabled()).thenReturn(true);

        ProviderResult<List<InstrumentSearchResult>> expectedResult =
                successfulResult("provider-two", "Provider Two");

        when(secondProvider.searchInstruments("RELIANCE"))
                .thenReturn(expectedResult);

        MarketDataProviderProperties properties =
                createProperties("provider-one", "provider-two");

        MarketDataProviderManager manager =
                new MarketDataProviderManager(
                        List.of(firstProvider, secondProvider),
                        properties
                );

        ProviderResult<List<InstrumentSearchResult>> result =
                manager.searchInstruments("RELIANCE");

        assertNotNull(result);
        assertSame(expectedResult, result);
        assertEquals("provider-two", result.getProviderCode());
        assertEquals("Provider Two", result.getProviderDisplayName());

        InOrder providerCallOrder =
                inOrder(firstProvider, secondProvider);

        providerCallOrder.verify(firstProvider)
                .searchInstruments("RELIANCE");

        providerCallOrder.verify(secondProvider)
                .searchInstruments("RELIANCE");
    }

    @Test
    void shouldThrowMarketDataUnavailableExceptionWhenAllProvidersFail() {
        MarketDataProvider firstProvider = mock(MarketDataProvider.class);
        MarketDataProvider secondProvider = mock(MarketDataProvider.class);

        when(firstProvider.getProviderCode()).thenReturn("provider-one");
        when(firstProvider.isEnabled()).thenReturn(true);

        ProviderException firstFailure =
                new ProviderException("Provider one failed");

        when(firstProvider.searchInstruments("RELIANCE"))
                .thenThrow(firstFailure);

        when(secondProvider.getProviderCode()).thenReturn("provider-two");
        when(secondProvider.isEnabled()).thenReturn(true);

        ProviderException secondFailure =
                new ProviderException("Provider two failed");

        when(secondProvider.searchInstruments("RELIANCE"))
                .thenThrow(secondFailure);

        MarketDataProviderProperties properties =
                createProperties("provider-one", "provider-two");

        MarketDataProviderManager manager =
                new MarketDataProviderManager(
                        List.of(firstProvider, secondProvider),
                        properties
                );

        MarketDataUnavailableException exception =
                assertThrows(
                        MarketDataUnavailableException.class,
                        () -> manager.searchInstruments("RELIANCE")
                );

        assertEquals(
                "All configured market-data providers failed",
                exception.getMessage()
        );

        assertSame(secondFailure, exception.getCause());

        verify(firstProvider).searchInstruments("RELIANCE");
        verify(secondProvider).searchInstruments("RELIANCE");
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenNoProvidersAreConfigured() {
        MarketDataProviderProperties properties = createProperties();

        MarketDataProviderManager manager =
                new MarketDataProviderManager(
                        List.of(),
                        properties
                );

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> manager.searchInstruments("RELIANCE")
                );

        assertEquals(
                "No providers are configured for instrument search",
                exception.getMessage()
        );
    }

    @Test
    void shouldSkipDisabledProviderAndUseNextEnabledProvider() {
        MarketDataProvider disabledProvider = mock(MarketDataProvider.class);
        MarketDataProvider enabledProvider = mock(MarketDataProvider.class);

        when(disabledProvider.getProviderCode())
                .thenReturn("provider-one");
        when(disabledProvider.isEnabled())
                .thenReturn(false);

        when(enabledProvider.getProviderCode())
                .thenReturn("provider-two");
        when(enabledProvider.isEnabled())
                .thenReturn(true);

        ProviderResult<List<InstrumentSearchResult>> expectedResult =
                successfulResult("provider-two", "Provider Two");

        when(enabledProvider.searchInstruments("RELIANCE"))
                .thenReturn(expectedResult);

        MarketDataProviderProperties properties =
                createProperties("provider-one", "provider-two");

        MarketDataProviderManager manager =
                new MarketDataProviderManager(
                        List.of(disabledProvider, enabledProvider),
                        properties
                );

        ProviderResult<List<InstrumentSearchResult>> result =
                manager.searchInstruments("RELIANCE");

        assertNotNull(result);
        assertSame(expectedResult, result);

        verify(disabledProvider, never())
                .searchInstruments(anyString());

        verify(enabledProvider)
                .searchInstruments("RELIANCE");
    }

    @Test
    void shouldSkipUnavailableProviderAndUseNextAvailableProvider() {
        MarketDataProvider availableProvider = mock(MarketDataProvider.class);

        when(availableProvider.getProviderCode())
                .thenReturn("provider-two");
        when(availableProvider.isEnabled())
                .thenReturn(true);

        ProviderResult<List<InstrumentSearchResult>> expectedResult =
                successfulResult("provider-two", "Provider Two");

        when(availableProvider.searchInstruments("RELIANCE"))
                .thenReturn(expectedResult);

        MarketDataProviderProperties properties =
                createProperties(
                        "provider-not-available",
                        "provider-two"
                );

        MarketDataProviderManager manager =
                new MarketDataProviderManager(
                        List.of(availableProvider),
                        properties
                );

        ProviderResult<List<InstrumentSearchResult>> result =
                manager.searchInstruments("RELIANCE");

        assertNotNull(result);
        assertSame(expectedResult, result);

        verify(availableProvider)
                .searchInstruments("RELIANCE");
    }

    private ProviderResult<List<InstrumentSearchResult>> successfulResult(
            String providerCode,
            String providerDisplayName) {

        return new ProviderResult<>(
                List.of(),
                providerCode,
                providerDisplayName
        );
    }

    private MarketDataProviderProperties createProperties(
            String... providerCodes) {

        MarketDataProviderProperties properties =
                new MarketDataProviderProperties();

        MarketDataProviderProperties.ProviderOrder providerOrder =
                new MarketDataProviderProperties.ProviderOrder();

        providerOrder.setInstrumentSearch(List.of(providerCodes));
        properties.setProviderOrder(providerOrder);

        return properties;
    }
}