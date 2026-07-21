package com.stockanalytics.provider.market;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.exception.ProviderException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DummyPrimaryMarketDataProvider implements MarketDataProvider {

    private static final String PROVIDER_CODE = "dummy-primary";
    private static final String DISPLAY_NAME = "Dummy Primary Provider";

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
        throw new ProviderException(
                "Simulated failure from " + DISPLAY_NAME
        );
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}