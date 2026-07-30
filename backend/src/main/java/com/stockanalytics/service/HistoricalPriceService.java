package com.stockanalytics.service;

import com.stockanalytics.domain.market.HistoricalPricePoint;
import com.stockanalytics.domain.market.HistoricalRange;
import com.stockanalytics.domain.market.MarketDataInterval;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.provider.market.MarketDataProviderManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class HistoricalPriceService {

    private final MarketDataProviderManager marketDataProviderManager;

    public HistoricalPriceService(
            MarketDataProviderManager marketDataProviderManager) {

        this.marketDataProviderManager = Objects.requireNonNull(
                marketDataProviderManager,
                "marketDataProviderManager must not be null"
        );
    }

    public ProviderResult<List<HistoricalPricePoint>> getHistoricalPrices(
            String symbol,
            HistoricalRange range,
            MarketDataInterval interval) {

        String validatedSymbol = requireNonBlank(
                symbol,
                "Symbol must not be blank"
        );

        Objects.requireNonNull(
                range,
                "Historical range must not be null"
        );

        Objects.requireNonNull(
                interval,
                "Market-data interval must not be null"
        );

        return marketDataProviderManager.getHistoricalPrices(
                validatedSymbol,
                range,
                interval
        );
    }

    private String requireNonBlank(
            String value,
            String message) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}