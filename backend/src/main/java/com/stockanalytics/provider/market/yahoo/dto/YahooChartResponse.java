package com.stockanalytics.provider.market.yahoo.dto;

import java.math.BigDecimal;
import java.util.List;

public record YahooChartResponse(
        Chart chart
) {

    public record Chart(
            List<Result> result,
            Error error
    ) {
    }

    public record Result(
            Meta meta,
            List<Long> timestamp,
            Indicators indicators
    ) {
    }

    public record Meta(
            String symbol,
            String exchangeName,
            String exchangeTimezoneName,
            String timezone,
            Integer gmtoffset
    ) {
    }

    public record Indicators(
            List<Quote> quote,
            List<AdjustedClose> adjclose
    ) {
    }

    public record Quote(
            List<BigDecimal> open,
            List<BigDecimal> high,
            List<BigDecimal> low,
            List<BigDecimal> close,
            List<Long> volume
    ) {
    }

    public record AdjustedClose(
            List<BigDecimal> adjclose
    ) {
    }

    public record Error(
            String code,
            String description
    ) {
    }
}