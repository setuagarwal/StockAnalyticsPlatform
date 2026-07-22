package com.stockanalytics.provider.market;

import com.stockanalytics.domain.Country;
import com.stockanalytics.domain.Exchange;
import com.stockanalytics.domain.InstrumentType;
import com.stockanalytics.dto.response.InstrumentSearchResult;
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
    public boolean isEnabled() {
        return true;
    }
}