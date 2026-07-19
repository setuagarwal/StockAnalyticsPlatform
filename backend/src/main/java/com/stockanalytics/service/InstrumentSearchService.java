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
import com.stockanalytics.provider.market.MarketDataProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class InstrumentSearchService {

    private static final int MINIMUM_QUERY_LENGTH = 2;

    private final MarketDataProvider marketDataProvider;

    public InstrumentSearchService(MarketDataProvider marketDataProvider) {
        this.marketDataProvider = Objects.requireNonNull(
                marketDataProvider,
                "marketDataProvider must not be null"
        );
    }

    public List<InstrumentSearchResult> search(String query) {
        String normalizedQuery = normalizeQuery(query);

        if (normalizedQuery.length() < MINIMUM_QUERY_LENGTH) {
            return List.of();
        }

        return marketDataProvider.searchInstruments(normalizedQuery);
    }

    private String normalizeQuery(String query) {
        if (query == null) {
            return "";
        }

        return query.trim();
    }
}