package com.stockanalytics.provider.market.yahoo;

import com.stockanalytics.config.provider.YahooProviderProperties;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.exception.ProviderException;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.provider.market.MarketDataProvider;
import com.stockanalytics.provider.market.yahoo.dto.YahooSearchResponse;
import com.stockanalytics.provider.market.yahoo.mapper.YahooInstrumentMapper;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.stockanalytics.provider.ProviderResult;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class YahooFinanceProvider implements MarketDataProvider {

    private static final String PROVIDER_CODE = "yahoo";
    private static final String DISPLAY_NAME = "Yahoo Finance";
    private static final String SEARCH_PATH = "/v1/finance/search";

    private final WebClient webClient;
    private final YahooInstrumentMapper yahooInstrumentMapper;
    private final YahooProviderProperties yahooProviderProperties;

    public YahooFinanceProvider(
            WebClient.Builder webClientBuilder,
            YahooInstrumentMapper yahooInstrumentMapper,
            YahooProviderProperties yahooProviderProperties) {

        this.yahooInstrumentMapper = Objects.requireNonNull(
                yahooInstrumentMapper,
                "yahooInstrumentMapper must not be null"
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
					if (response != null && response.quotes() != null) {
						response.quotes()
						.stream()
						.filter(Objects::nonNull)
						.forEach(quote ->
								System.out.println(
								"Yahoo raw result: "
                                    + quote.symbol()
                                    + " | "
                                    + quote.exchange()
                                    + " | "
                                    + quote.shortname()
													)
								);
																		}
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
                            + exception.getStatusCode().value(),
                    exception
            );

        } catch (WebClientRequestException exception) {
            throw new ProviderException(
                    "Unable to connect to Yahoo Finance",
                    exception
            );

        } catch (DecodingException exception) {
            throw new ProviderException(
                    "Yahoo Finance returned an invalid response",
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

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private String requireNonBlank(
            String value,
            String message) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value;
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