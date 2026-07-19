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
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.provider.ProviderResult;
import org.springframework.stereotype.Component;

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

        RuntimeException lastFailure = null;

        for (String providerCode : providerOrder) {
            MarketDataProvider provider = providersByCode.get(providerCode);

            if (provider == null || !provider.isEnabled()) {
                continue;
            }

            try {
                List<InstrumentSearchResult> results =
                        provider.searchInstruments(query);

                return new ProviderResult<>(
                        results,
                        provider.getProviderCode(),
                        provider.getDisplayName()
                );
            } catch (RuntimeException exception) {
                lastFailure = exception;
            }
        }

        throw new IllegalStateException(
                "All configured market-data providers failed",
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
            throw new IllegalArgumentException(message);
        }

        return value;
    }
}