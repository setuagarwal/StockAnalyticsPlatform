package com.stockanalytics.provider.market.yahoo.mapper;

import com.stockanalytics.domain.market.HistoricalPricePoint;
import com.stockanalytics.provider.market.yahoo.dto.YahooChartResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Component
public class YahooHistoricalPriceMapper {

    public List<HistoricalPricePoint> toHistoricalPricePoints(
            YahooChartResponse response) {

        if (response == null
                || response.chart() == null
                || response.chart().result() == null
                || response.chart().result().isEmpty()) {

            return Collections.emptyList();
        }

        YahooChartResponse.Result result =
                response.chart().result().getFirst();

        if (result == null
                || result.timestamp() == null
                || result.timestamp().isEmpty()
                || result.indicators() == null
                || result.indicators().quote() == null
                || result.indicators().quote().isEmpty()) {

            return Collections.emptyList();
        }

        YahooChartResponse.Quote quote =
                result.indicators().quote().getFirst();

        if (quote == null) {
            return Collections.emptyList();
        }

        YahooChartResponse.AdjustedClose adjustedClose =
                getAdjustedClose(result);

        return IntStream.range(0, result.timestamp().size())
                .mapToObj(index -> mapPricePoint(
                        result,
                        quote,
                        adjustedClose,
                        index
                ))
                .filter(Objects::nonNull)
                .toList();
    }

    private HistoricalPricePoint mapPricePoint(
            YahooChartResponse.Result result,
            YahooChartResponse.Quote quote,
            YahooChartResponse.AdjustedClose adjustedClose,
            int index) {

        Long timestamp = getValue(
                result.timestamp(),
                index
        );

        BigDecimal open = getValue(
                quote.open(),
                index
        );

        BigDecimal high = getValue(
                quote.high(),
                index
        );

        BigDecimal low = getValue(
                quote.low(),
                index
        );

        BigDecimal close = getValue(
                quote.close(),
                index
        );

        Long volume = getValue(
                quote.volume(),
                index
        );

        BigDecimal adjustedCloseValue =
                adjustedClose == null
                        ? null
                        : getValue(
                                adjustedClose.adjclose(),
                                index
                        );

        if (timestamp == null
                || open == null
                || high == null
                || low == null
                || close == null) {

            return null;
        }

        return new HistoricalPricePoint(
                toLocalDate(timestamp, result.meta()),
                open,
                high,
                low,
                close,
                adjustedCloseValue,
                volume
        );
    }

    private YahooChartResponse.AdjustedClose getAdjustedClose(
            YahooChartResponse.Result result) {

        if (result.indicators().adjclose() == null
                || result.indicators().adjclose().isEmpty()) {

            return null;
        }

        return result.indicators()
                .adjclose()
                .getFirst();
    }

    private LocalDate toLocalDate(
            Long epochSeconds,
            YahooChartResponse.Meta meta) {

        return Instant.ofEpochSecond(epochSeconds)
                .atZone(resolveMarketZone(meta))
                .toLocalDate();
    }

    private ZoneId resolveMarketZone(
            YahooChartResponse.Meta meta) {

        if (meta != null
                && meta.exchangeTimezoneName() != null
                && !meta.exchangeTimezoneName().isBlank()) {

            try {
                return ZoneId.of(
                        meta.exchangeTimezoneName()
                );
            } catch (DateTimeException ignored) {
                // Fall back to UTC when Yahoo provides an invalid zone.
            }
        }

        return ZoneOffset.UTC;
    }

    private <T> T getValue(
            List<T> values,
            int index) {

        if (values == null
                || index < 0
                || index >= values.size()) {

            return null;
        }

        return values.get(index);
    }
}