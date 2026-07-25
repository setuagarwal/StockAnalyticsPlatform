package com.stockanalytics.config.search;

import com.stockanalytics.domain.Exchange;
import com.stockanalytics.domain.InstrumentType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Configurable preferences used while ranking instrument search results.
 *
 * Exchange and instrument-type preference scores are loaded
 * from application.yml.
 */
@Component
@ConfigurationProperties(prefix = "search.preference")
public class SearchPreferenceProperties {

    private int defaultExchangeScore = 0;

    private Map<Exchange, Integer> exchangePreferenceScores =
            new EnumMap<>(Exchange.class);

    private int defaultInstrumentTypeScore = 0;

    private Map<InstrumentType, Integer> instrumentTypePreferenceScores =
            new EnumMap<>(InstrumentType.class);

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

    public int getExchangePreferenceScore(Exchange exchange) {
        if (exchange == null) {
            return defaultExchangeScore;
        }

        return exchangePreferenceScores.getOrDefault(
                exchange,
                defaultExchangeScore
        );
    }

    public int getDefaultInstrumentTypeScore() {
        return defaultInstrumentTypeScore;
    }

    public void setDefaultInstrumentTypeScore(
            int defaultInstrumentTypeScore
    ) {
        this.defaultInstrumentTypeScore = defaultInstrumentTypeScore;
    }

    public Map<InstrumentType, Integer>
            getInstrumentTypePreferenceScores() {

        return Collections.unmodifiableMap(
                instrumentTypePreferenceScores
        );
    }

    public void setInstrumentTypePreferenceScores(
            Map<InstrumentType, Integer>
                    instrumentTypePreferenceScores
    ) {
        this.instrumentTypePreferenceScores =
                new EnumMap<>(InstrumentType.class);

        if (instrumentTypePreferenceScores != null) {
            this.instrumentTypePreferenceScores.putAll(
                    instrumentTypePreferenceScores
            );
        }
    }

    /**
     * Returns the configured preference score for an instrument type.
     *
     * Unconfigured and null types receive the configured default score.
     */
    public int getInstrumentTypePreferenceScore(
            InstrumentType instrumentType
    ) {
        if (instrumentType == null) {
            return defaultInstrumentTypeScore;
        }

        return instrumentTypePreferenceScores.getOrDefault(
                instrumentType,
                defaultInstrumentTypeScore
        );
    }
}