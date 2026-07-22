package com.stockanalytics.provider.market.yahoo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YahooSearchResponse(
        List<YahooQuote> quotes
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YahooQuote(
            String symbol,
            String shortname,
            String longname,
            String exchange,
            String quoteType
    ) {
    }
}