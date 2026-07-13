package com.stockanalytics.provider;

import com.stockanalytics.dto.InstrumentSearchResult;

import java.util.List;

/**
 * Defines operations supported by an external market-data provider.
 *
 * Implementations may connect to Yahoo Finance, Alpha Vantage,
 * Twelve Data, or another compatible provider.
 */
public interface MarketDataProvider {

    /**
     * Searches for instruments matching a company name or symbol.
     *
     * @param query search text entered by the user
     * @return matching instruments from supported exchanges
     */
    List<InstrumentSearchResult> searchInstruments(String query);

    /**
     * Returns the unique name of this provider.
     *
     * @return provider name
     */
    String getProviderName();

    /**
     * Indicates whether the provider is currently enabled.
     *
     * @return {@code true} when the provider may be used
     */
    boolean isEnabled();
}