package com.stockanalytics.config.search;

import com.stockanalytics.domain.Exchange;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Configurable preferences used while ranking instrument search results.
 *
 * Exchange preference scores are loaded from application.yml.
 */
@Component
@ConfigurationProperties(prefix = "search.preference")
public class SearchPreferenceProperties {

    private int defaultExchangeScore = 0;

    private Map<Exchange, Integer> exchangePreferenceScores =
            new EnumMap<>(Exchange.class);

    public int getDefaultExchangeScore() {
        return defaultExchangeScore;
    }

    public void setDefaultExchangeScore(int defaultExchangeScore) {
        this.defaultExchangeScore = defaultExchangeScore;
    }

    public Map<Exchange, Integer> getExchangePreferenceScores() {
        return Collections.unmodifiableMap(exchangePreferenceScores);
    }

    public void setExchangePreferenceScores(
            Map<Exchange, Integer> exchangePreferenceScores
    ) {
        this.exchangePreferenceScores = new EnumMap<>(Exchange.class);

        if (exchangePreferenceScores != null) {
            this.exchangePreferenceScores.putAll(exchangePreferenceScores);
        }
    }

    /**
     * Returns the configured preference score for an exchange.
     *
     * Unconfigured and null exchanges receive the configured default score.
     */
    public int getExchangePreferenceScore(Exchange exchange) {
        if (exchange == null) {
            return defaultExchangeScore;
        }

        return exchangePreferenceScores.getOrDefault(
                exchange,
                defaultExchangeScore
        );
    }
}
