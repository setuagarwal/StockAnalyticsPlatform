package com.stockanalytics.provider.market;

import com.stockanalytics.dto.response.InstrumentSearchResult;
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
    public List<InstrumentSearchResult> searchInstruments(String query) {
        return List.of(
                new InstrumentSearchResult(
						"NSE",
                        "TCS",
                        "Tata Consultancy Services",
                        "STOCK"
                )
        );
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}