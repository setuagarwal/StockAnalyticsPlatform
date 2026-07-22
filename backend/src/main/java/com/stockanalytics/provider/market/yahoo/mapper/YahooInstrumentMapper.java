package com.stockanalytics.provider.market.yahoo.mapper;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.provider.market.yahoo.dto.YahooSearchResponse;
import org.springframework.stereotype.Component;

@Component
public class YahooInstrumentMapper {

    public InstrumentSearchResult toInstrumentSearchResult(
            YahooSearchResponse.YahooQuote quote) {

        String name = quote.longname();

        if (name == null || name.isBlank()) {
            name = quote.shortname();
        }

        return new InstrumentSearchResult(
                quote.exchange(),
                quote.symbol(),
                name,
                quote.quoteType()
        );
    }
}