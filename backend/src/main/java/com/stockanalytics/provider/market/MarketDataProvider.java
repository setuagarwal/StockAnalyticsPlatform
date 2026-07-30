package com.stockanalytics.provider.market;

import com.stockanalytics.domain.market.HistoricalPricePoint;
import com.stockanalytics.domain.market.HistoricalRange;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.exception.ProviderException;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.domain.market.MarketDataInterval;

import java.util.List;

/**
 * Defines the contract implemented by market-data providers.
 */
public interface MarketDataProvider {

    /**
     * Searches for instruments using this provider.
     *
     * @param query symbol or company-name search text
     * @return matching financial instruments and provider information
     */
    ProviderResult<List<InstrumentSearchResult>> searchInstruments(
            String query
    );

    /**
     * Retrieves historical prices for an instrument.
     *
     * Providers should override this method when they support historical
     * market data. Until then, the default implementation reports that the
     * capability is unsupported.
     *
     * @param symbol provider-compatible instrument symbol
     * @param range requested historical range
     * @return historical prices and provider information
     */
    default ProviderResult<List<HistoricalPricePoint>> getHistoricalPrices(
            String symbol,
            HistoricalRange range,
			MarketDataInterval interval
    ) {
        throw new ProviderException(
                "Historical prices are not supported by "
                        + getDisplayName()
        );
    }

    /**
     * Returns the stable provider code used in configuration.
     *
     * @return provider configuration code
     */
    String getProviderCode();

    /**
     * Returns the provider name suitable for display.
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
