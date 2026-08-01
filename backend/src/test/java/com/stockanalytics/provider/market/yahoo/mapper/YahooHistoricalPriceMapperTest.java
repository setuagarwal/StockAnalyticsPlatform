package com.stockanalytics.provider.market.yahoo.mapper;

import com.stockanalytics.domain.market.HistoricalPricePoint;
import com.stockanalytics.provider.market.yahoo.dto.YahooChartResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class YahooHistoricalPriceMapperTest {

    private final YahooHistoricalPriceMapper mapper =
            new YahooHistoricalPriceMapper();

    @Test
    void shouldMapYahooResponseToHistoricalPricePoints() {

        YahooChartResponse response = createValidResponse();

        List<HistoricalPricePoint> result =
                mapper.toHistoricalPricePoints(response);

        assertEquals(1, result.size());

        HistoricalPricePoint point = result.getFirst();

        assertEquals(
                LocalDate.of(2025, 7, 30),
                point.date()
        );

        assertEquals(
                BigDecimal.valueOf(1500.00),
                point.open()
        );

        assertEquals(
                BigDecimal.valueOf(1510.00),
                point.high()
        );

        assertEquals(
                BigDecimal.valueOf(1490.00),
                point.low()
        );

        assertEquals(
                BigDecimal.valueOf(1505.00),
                point.close()
        );

        assertEquals(
                BigDecimal.valueOf(1504.50),
                point.adjustedClose()
        );

        assertEquals(
                1_000_000L,
                point.volume()
        );
    }

    @Test
    void shouldReturnEmptyListWhenResponseIsNull() {

        List<HistoricalPricePoint> result =
                mapper.toHistoricalPricePoints(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSkipPricePointWhenOpenPriceIsMissing() {

        YahooChartResponse response =
                createResponseWithMissingOpenPrice();

        List<HistoricalPricePoint> result =
                mapper.toHistoricalPricePoints(response);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFallbackToUtcWhenTimezoneIsInvalid() {

        YahooChartResponse response =
                createResponseWithInvalidTimezone();

        assertDoesNotThrow(() ->
                mapper.toHistoricalPricePoints(response));
    }

    private YahooChartResponse createValidResponse() {

        return createResponse("Asia/Kolkata",
                List.of(BigDecimal.valueOf(1500.00)));
    }

    private YahooChartResponse createResponseWithMissingOpenPrice() {

        return createResponse("Asia/Kolkata",
                Collections.<BigDecimal>singletonList(null));
    }

    private YahooChartResponse createResponseWithInvalidTimezone() {

        return createResponse("INVALID_ZONE",
                List.of(BigDecimal.valueOf(1500.00)));
    }

    private YahooChartResponse createResponse(
            String timezone,
            List<BigDecimal> openPrices) {

        YahooChartResponse.Meta meta =
                new YahooChartResponse.Meta(
                        "RELIANCE.NS",
                        "NSE",
                        timezone,
                        timezone,
                        19800
                );

        YahooChartResponse.Quote quote =
                new YahooChartResponse.Quote(
                        openPrices,
                        List.of(BigDecimal.valueOf(1510.00)),
                        List.of(BigDecimal.valueOf(1490.00)),
                        List.of(BigDecimal.valueOf(1505.00)),
                        List.of(1_000_000L)
                );

        YahooChartResponse.AdjustedClose adjustedClose =
                new YahooChartResponse.AdjustedClose(
                        List.of(BigDecimal.valueOf(1504.50))
                );

        YahooChartResponse.Indicators indicators =
                new YahooChartResponse.Indicators(
                        List.of(quote),
                        List.of(adjustedClose)
                );

        YahooChartResponse.Result result =
                new YahooChartResponse.Result(
                        meta,
                        List.of(1753813800L),
                        indicators
                );

        YahooChartResponse.Chart chart =
                new YahooChartResponse.Chart(
                        List.of(result),
                        null
                );

        return new YahooChartResponse(chart);
    }
}