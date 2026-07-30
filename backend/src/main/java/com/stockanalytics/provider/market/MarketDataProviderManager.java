/*
 * Project : Stock Analytics Platform
 *
 * Copyright (c) 2026.
 *
 * Description:
 * Selects market-data providers according to the configured order
 * and automatically falls back to the next provider when one fails.
 */

package com.stockanalytics.provider.market;

import com.stockanalytics.config.MarketDataProviderProperties;
import com.stockanalytics.domain.market.HistoricalPricePoint;
import com.stockanalytics.domain.market.HistoricalRange;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.exception.MarketDataUnavailableException;
import com.stockanalytics.exception.ProviderException;
import com.stockanalytics.provider.ProviderResult;
import org.springframework.stereotype.Component;
import com.stockanalytics.domain.market.MarketDataInterval;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class MarketDataProviderManager {

    private final Map<String, MarketDataProvider> providersByCode;
    private final MarketDataProviderProperties providerProperties;

    public MarketDataProviderManager(
            List<MarketDataProvider> providers,
            MarketDataProviderProperties providerProperties) {

        this.providersByCode = buildProviderMap(providers);
        this.providerProperties = Objects.requireNonNull(
                providerProperties,
                "providerProperties must not be null"
        );
    }

    public ProviderResult<List<InstrumentSearchResult>> searchInstruments(
            String query) {

        List<String> providerOrder = providerProperties
                .getProviderOrder()
                .getInstrumentSearch();

        if (providerOrder.isEmpty()) {
            throw new IllegalStateException(
                    "No providers are configured for instrument search"
            );
        }

        ProviderException lastFailure = null;

        for (String providerCode : providerOrder) {
            MarketDataProvider provider = providersByCode.get(providerCode);

            if (provider == null || !provider.isEnabled()) {
                continue;
            }

            try {
                return provider.searchInstruments(query);
            } catch (ProviderException exception) {
                lastFailure = exception;
            }
        }

        throw new MarketDataUnavailableException(
                "All configured market-data providers failed for instrument search",
                lastFailure
        );
    }

    public ProviderResult<List<HistoricalPricePoint>> getHistoricalPrices(
            String symbol,
            HistoricalRange range,
			MarketDataInterval interval) {

        List<String> providerOrder = providerProperties
                .getProviderOrder()
                .getInstrumentSearch();

        if (providerOrder.isEmpty()) {
            throw new IllegalStateException(
                    "No providers are configured for historical prices"
            );
        }

        ProviderException lastFailure = null;

        for (String providerCode : providerOrder) {
            MarketDataProvider provider = providersByCode.get(providerCode);

            if (provider == null || !provider.isEnabled()) {
                continue;
            }

            try {
                return provider.getHistoricalPrices(symbol, range, interval);
            } catch (ProviderException exception) {
                lastFailure = exception;
            }
        }

        throw new MarketDataUnavailableException(
                "All configured market-data providers failed for historical prices",
                lastFailure
        );
    }

    private Map<String, MarketDataProvider> buildProviderMap(
            List<MarketDataProvider> providers) {

        Objects.requireNonNull(providers, "providers must not be null");

        Map<String, MarketDataProvider> providerMap = new LinkedHashMap<>();

        for (MarketDataProvider provider : providers) {
            MarketDataProvider nonNullProvider = Objects.requireNonNull(
                    provider,
                    "providers must not contain null values"
            );

            String providerCode = requireNonBlank(
                    nonNullProvider.getProviderCode(),
                    "Provider code must not be blank"
            );

            MarketDataProvider existingProvider =
                    providerMap.putIfAbsent(providerCode, nonNullProvider);

            if (existingProvider != null) {
                throw new IllegalStateException(
                        "Duplicate market-data provider code: " + providerCode
                );
            }
        }

        return Map.copyOf(providerMap);
    }

    private String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(message);
        }

        return value;
    }
}