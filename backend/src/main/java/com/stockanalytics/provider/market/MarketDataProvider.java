/*
 * Project : Stock Analytics Platform
 *
 * Copyright (c) 2026.
 *
 * Description:
 * Defines the contract implemented by market-data providers.
 */

package com.stockanalytics.provider.market;

import com.stockanalytics.dto.response.InstrumentSearchResult;

import java.util.List;

public interface MarketDataProvider {

    /**
     * Searches for instruments using this provider.
     *
     * @param query symbol or company-name search text
     * @return matching financial instruments
     */
    List<InstrumentSearchResult> searchInstruments(String query);

    /**
     * Returns the stable provider code used in configuration.
     *
     * Example: yahoo-finance
     *
     * @return provider configuration code
     */
    String getProviderCode();

    /**
     * Returns the provider name suitable for display on the UI.
     *
     * Example: Yahoo Finance
     *
     * @return provider display name
     */
    String getDisplayName();

    /**
     * Indicates whether this provider is currently enabled.
     *
     * @return true when the provider may be used
     */
    boolean isEnabled();
}