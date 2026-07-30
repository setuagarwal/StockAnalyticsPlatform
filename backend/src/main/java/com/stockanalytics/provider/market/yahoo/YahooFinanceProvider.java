package com.stockanalytics.provider.market.yahoo;

import com.stockanalytics.config.provider.YahooProviderProperties;
import com.stockanalytics.domain.market.HistoricalPricePoint;
import com.stockanalytics.domain.market.HistoricalRange;
import com.stockanalytics.domain.market.MarketDataInterval;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.exception.ProviderException;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.provider.market.MarketDataProvider;
import com.stockanalytics.provider.market.yahoo.dto.YahooChartResponse;
import com.stockanalytics.provider.market.yahoo.dto.YahooSearchResponse;
import com.stockanalytics.provider.market.yahoo.mapper.YahooHistoricalPriceMapper;
import com.stockanalytics.provider.market.yahoo.mapper.YahooInstrumentMapper;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class YahooFinanceProvider implements MarketDataProvider {

    private static final String PROVIDER_CODE = "yahoo";
    private static final String DISPLAY_NAME = "Yahoo Finance";

    private static final String SEARCH_PATH =
            "/v1/finance/search";

    private static final String HISTORICAL_PRICE_PATH =
            "/v8/finance/chart/{symbol}";

    private final WebClient webClient;
    private final YahooInstrumentMapper yahooInstrumentMapper;
    private final YahooHistoricalPriceMapper yahooHistoricalPriceMapper;
    private final YahooProviderProperties yahooProviderProperties;

    public YahooFinanceProvider(
            WebClient.Builder webClientBuilder,
            YahooInstrumentMapper yahooInstrumentMapper,
            YahooHistoricalPriceMapper yahooHistoricalPriceMapper,
            YahooProviderProperties yahooProviderProperties) {

        this.yahooInstrumentMapper = Objects.requireNonNull(
                yahooInstrumentMapper,
                "yahooInstrumentMapper must not be null"
        );

        this.yahooHistoricalPriceMapper = Objects.requireNonNull(
                yahooHistoricalPriceMapper,
                "yahooHistoricalPriceMapper must not be null"
        );

        this.yahooProviderProperties = Objects.requireNonNull(
                yahooProviderProperties,
                "yahooProviderProperties must not be null"
        );

        Objects.requireNonNull(
                webClientBuilder,
                "webClientBuilder must not be null"
        );

        String baseUrl = requireNonBlank(
                yahooProviderProperties.getBaseUrl(),
                "Yahoo base URL must not be blank"
        );

        /*
         * clone() prevents this provider from modifying the shared
         * WebClient.Builder used by other external providers.
         */
        this.webClient = webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public ProviderResult<List<InstrumentSearchResult>> searchInstruments(
            String query) {

        String validatedQuery = requireNonBlank(
                query,
                "Search query must not be blank"
        );

        try {
            YahooSearchResponse response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(SEARCH_PATH)
                            .queryParam("q", validatedQuery)
                            .build())
                    .retrieve()
                    .bodyToMono(YahooSearchResponse.class)
                    .block();

            List<InstrumentSearchResult> results =
                    mapSearchResults(response);

            return new ProviderResult<>(
                    results,
                    getProviderCode(),
                    getDisplayName()
            );

        } catch (WebClientResponseException exception) {
            throw new ProviderException(
                    "Yahoo Finance returned HTTP status "
                            + exception.getStatusCode().value()
                            + " while searching for instruments",
                    exception
            );

        } catch (WebClientRequestException exception) {
            throw new ProviderException(
                    "Unable to connect to Yahoo Finance "
                            + "while searching for instruments",
                    exception
            );

        } catch (DecodingException exception) {
            throw new ProviderException(
                    "Yahoo Finance returned an invalid "
                            + "instrument-search response",
                    exception
            );
        }
    }

    @Override
    public ProviderResult<List<HistoricalPricePoint>> getHistoricalPrices(
            String symbol,
            HistoricalRange range,
            MarketDataInterval interval) {

        String validatedSymbol = requireNonBlank(
                symbol,
                "Symbol must not be blank"
        );

        Objects.requireNonNull(
                range,
                "Historical range must not be null"
        );

        Objects.requireNonNull(
                interval,
                "Market-data interval must not be null"
        );

        validateRangeAndInterval(range, interval);

        try {
            YahooChartResponse response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(HISTORICAL_PRICE_PATH)
                            .queryParam(
                                    "range",
                                    toYahooRange(range)
                            )
                            .queryParam(
                                    "interval",
                                    toYahooInterval(interval)
                            )
                            .queryParam(
                                    "includeAdjustedClose",
                                    true
                            )
                            .queryParam(
                                    "events",
                                    "div,splits"
                            )
                            .build(validatedSymbol))
                    .retrieve()
                    .bodyToMono(YahooChartResponse.class)
                    .block();

            validateHistoricalPriceResponse(
                    response,
                    validatedSymbol
            );

            List<HistoricalPricePoint> historicalPrices =
                    yahooHistoricalPriceMapper
                            .toHistoricalPricePoints(response);

            return new ProviderResult<>(
                    historicalPrices,
                    getProviderCode(),
                    getDisplayName()
            );

        } catch (WebClientResponseException exception) {
            throw new ProviderException(
                    "Yahoo Finance returned HTTP status "
                            + exception.getStatusCode().value()
                            + " while retrieving historical prices for "
                            + validatedSymbol,
                    exception
            );

        } catch (WebClientRequestException exception) {
            throw new ProviderException(
                    "Unable to connect to Yahoo Finance "
                            + "while retrieving historical prices for "
                            + validatedSymbol,
                    exception
            );

        } catch (DecodingException exception) {
            throw new ProviderException(
                    "Yahoo Finance returned an invalid "
                            + "historical-price response for "
                            + validatedSymbol,
                    exception
            );
        }
    }

    private List<InstrumentSearchResult> mapSearchResults(
            YahooSearchResponse response) {

        if (response == null || response.quotes() == null) {
            return Collections.emptyList();
        }

        return response.quotes()
                .stream()
                .filter(Objects::nonNull)
                .filter(this::hasRequiredFields)
                .map(yahooInstrumentMapper::toInstrumentSearchResult)
                .toList();
    }

    private boolean hasRequiredFields(
            YahooSearchResponse.YahooQuote quote) {

        return isNotBlank(quote.symbol())
                && isNotBlank(quote.exchange())
                && isNotBlank(quote.quoteType())
                && (
                    isNotBlank(quote.longname())
                    || isNotBlank(quote.shortname())
                );
    }

    private void validateHistoricalPriceResponse(
            YahooChartResponse response,
            String symbol) {

        if (response == null || response.chart() == null) {
            throw new ProviderException(
                    "Yahoo Finance returned an empty "
                            + "historical-price response for "
                            + symbol
            );
        }

        YahooChartResponse.Error error =
                response.chart().error();

        if (error != null) {
            String errorDescription = isNotBlank(error.description())
                    ? error.description()
                    : "Unknown Yahoo Finance error";

            throw new ProviderException(
                    "Yahoo Finance historical-price request failed for "
                            + symbol
                            + ": "
                            + errorDescription
            );
        }

        if (response.chart().result() == null
                || response.chart().result().isEmpty()) {

            throw new ProviderException(
                    "Yahoo Finance returned no historical-price data for "
                            + symbol
            );
        }
    }

    /*
     * Intraday timestamp support will be implemented separately.
     * For now, only daily or longer intervals are allowed because
     * HistoricalPricePoint currently stores LocalDate.
     */
    private void validateRangeAndInterval(
            HistoricalRange range,
            MarketDataInterval interval) {

        boolean intradayInterval = switch (interval) {
            case ONE_MINUTE,
                 TWO_MINUTES,
                 FIVE_MINUTES,
                 FIFTEEN_MINUTES,
                 THIRTY_MINUTES,
                 ONE_HOUR -> true;

            case ONE_DAY,
                 ONE_WEEK,
                 ONE_MONTH -> false;
        };

        if (intradayInterval) {
            throw new ProviderException(
                    "Intraday intervals are not supported yet. "
                            + "They will be implemented as part of "
                            + "the dedicated intraday changes."
            );
        }

        /*
         * ONE_DAY and FIVE_DAYS ranges are allowed with daily candles.
         * This validation method can be extended later if a provider
         * requires stricter range and interval combinations.
         */
    }

    private String toYahooRange(HistoricalRange range) {

        return switch (range) {
            case ONE_DAY -> "1d";
            case FIVE_DAYS -> "5d";
            case ONE_MONTH -> "1mo";
            case THREE_MONTHS -> "3mo";
            case SIX_MONTHS -> "6mo";
            case ONE_YEAR -> "1y";
            case TWO_YEARS -> "2y";
            case FIVE_YEARS -> "5y";
            case TEN_YEARS -> "10y";
            case MAX -> "max";
        };
    }

    private String toYahooInterval(
            MarketDataInterval interval) {

        return switch (interval) {
            case ONE_MINUTE -> "1m";
            case TWO_MINUTES -> "2m";
            case FIVE_MINUTES -> "5m";
            case FIFTEEN_MINUTES -> "15m";
            case THIRTY_MINUTES -> "30m";
            case ONE_HOUR -> "1h";
            case ONE_DAY -> "1d";
            case ONE_WEEK -> "1wk";
            case ONE_MONTH -> "1mo";
        };
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private String requireNonBlank(
            String value,
            String message) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    @Override
    public String getProviderCode() {
        return PROVIDER_CODE;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public boolean isEnabled() {
        return yahooProviderProperties.isEnabled();
    }
}