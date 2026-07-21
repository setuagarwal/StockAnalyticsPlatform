package com.stockanalytics.provider.market;

import com.stockanalytics.config.MarketDataProviderProperties;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.exception.MarketDataUnavailableException;
import com.stockanalytics.exception.ProviderException;
import com.stockanalytics.provider.ProviderResult;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarketDataProviderManagerTest {

    @Test
    void shouldUseFirstConfiguredProviderWhenItSucceeds() {
        MarketDataProvider firstProvider = mock(MarketDataProvider.class);
        MarketDataProvider secondProvider = mock(MarketDataProvider.class);

        when(firstProvider.getProviderCode()).thenReturn("provider-one");
        when(firstProvider.getDisplayName()).thenReturn("Provider One");
        when(firstProvider.isEnabled()).thenReturn(true);

        when(secondProvider.getProviderCode()).thenReturn("provider-two");

        List<InstrumentSearchResult> expectedResults = List.of();
        when(firstProvider.searchInstruments("RELIANCE"))
                .thenReturn(expectedResults);

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
        when(secondProvider.getDisplayName()).thenReturn("Provider Two");
        when(secondProvider.isEnabled()).thenReturn(true);

        List<InstrumentSearchResult> expectedResults = List.of();
        when(secondProvider.searchInstruments("RELIANCE"))
                .thenReturn(expectedResults);

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
        when(enabledProvider.getDisplayName())
                .thenReturn("Provider Two");
        when(enabledProvider.isEnabled())
                .thenReturn(true);
        when(enabledProvider.searchInstruments("RELIANCE"))
                .thenReturn(List.of());

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
        when(availableProvider.getDisplayName())
                .thenReturn("Provider Two");
        when(availableProvider.isEnabled())
                .thenReturn(true);
        when(availableProvider.searchInstruments("RELIANCE"))
                .thenReturn(List.of());

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

        verify(availableProvider)
                .searchInstruments("RELIANCE");
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