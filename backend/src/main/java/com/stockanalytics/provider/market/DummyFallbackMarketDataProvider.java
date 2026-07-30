package com.stockanalytics.provider.market;

import com.stockanalytics.domain.Country;
import com.stockanalytics.domain.Exchange;
import com.stockanalytics.domain.InstrumentType;
import com.stockanalytics.domain.market.HistoricalPricePoint;
import com.stockanalytics.domain.market.HistoricalRange;
import com.stockanalytics.domain.market.MarketDataInterval;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.exception.ProviderException;
import com.stockanalytics.provider.ProviderResult;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DummyFallbackMarketDataProvider implements MarketDataProvider {

    private static final String PROVIDER_CODE = "dummy-fallback";
    private static final String DISPLAY_NAME = "Dummy Fallback Provider";

    @Override
    public String getProviderCode() {
        return PROVIDER_CODE;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public ProviderResult<List<InstrumentSearchResult>> searchInstruments(
            String query) {

        List<InstrumentSearchResult> results = List.of(
                new InstrumentSearchResult(
                        Exchange.NSE,
                        "TCS",
                        "Tata Consultancy Services",
                        InstrumentType.EQUITY,
                        Country.INDIA
                )
        );

        return new ProviderResult<>(
                results,
                PROVIDER_CODE,
                DISPLAY_NAME
        );
    }

    @Override
    public ProviderResult<List<HistoricalPricePoint>> getHistoricalPrices(
            String symbol,
            HistoricalRange range,
            MarketDataInterval interval) {

        throw new ProviderException(
                "Historical prices are not implemented for " + DISPLAY_NAME
        );
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}