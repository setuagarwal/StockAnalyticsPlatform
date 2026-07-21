/*
 * Project : Stock Analytics Platform
 *
 * Copyright (c) 2026.
 *
 * Description:
 * Coordinates instrument search requests through the configured
 * market-data provider.
 */

package com.stockanalytics.service;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.provider.market.MarketDataProviderManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class InstrumentSearchService {

    private final MarketDataProviderManager providerManager;

    public InstrumentSearchService(
            MarketDataProviderManager providerManager) {

        this.providerManager = Objects.requireNonNull(
                providerManager,
                "providerManager must not be null"
        );
    }

    public ProviderResult<List<InstrumentSearchResult>> search(
            String query) {

        return providerManager.searchInstruments(query);
    }
}