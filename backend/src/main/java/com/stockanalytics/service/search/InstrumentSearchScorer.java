package com.stockanalytics.service.search;

import com.stockanalytics.config.search.SearchPreferenceProperties;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Calculates a provider-independent relevance score for an instrument
 * search result.
 *
 * Higher scores represent a stronger match with the user's search query.
 */
@Component
public class InstrumentSearchScorer {

    private static final int EXACT_SYMBOL_SCORE = 100;
    private static final int SYMBOL_STARTS_WITH_SCORE = 90;
    private static final int EXACT_NAME_SCORE = 85;
    private static final int NAME_STARTS_WITH_SCORE = 75;
    private static final int SYMBOL_CONTAINS_SCORE = 65;
    private static final int NAME_CONTAINS_SCORE = 55;
    private static final int NO_MATCH_SCORE = 0;

    private final SearchPreferenceProperties searchPreferenceProperties;

    public InstrumentSearchScorer(
            SearchPreferenceProperties searchPreferenceProperties
    ) {
        this.searchPreferenceProperties = searchPreferenceProperties;
    }

    public int calculateScore(
            String query,
            InstrumentSearchResult result
    ) {
        if (result == null) {
            return NO_MATCH_SCORE;
        }

        String normalizedQuery = normalize(query);

        if (normalizedQuery.isEmpty()) {
            return NO_MATCH_SCORE;
        }

        String normalizedSymbol = normalize(result.symbol());
        String normalizedName = normalize(result.name());

        int relevanceScore = calculateRelevanceScore(
                normalizedQuery,
                normalizedSymbol,
                normalizedName
        );

        int exchangePreferenceScore =
                searchPreferenceProperties.getExchangePreferenceScore(
                        result.exchange()
                );

        int totalScore = relevanceScore + exchangePreferenceScore;

        return totalScore;
    }

    private int calculateRelevanceScore(
            String normalizedQuery,
            String normalizedSymbol,
            String normalizedName
    ) {
        if (normalizedSymbol.equals(normalizedQuery)) {
            return EXACT_SYMBOL_SCORE;
        }

        if (normalizedSymbol.startsWith(normalizedQuery)) {
            return SYMBOL_STARTS_WITH_SCORE;
        }

        if (normalizedName.equals(normalizedQuery)) {
            return EXACT_NAME_SCORE;
        }

        if (normalizedName.startsWith(normalizedQuery)) {
            return NAME_STARTS_WITH_SCORE;
        }

        if (normalizedSymbol.contains(normalizedQuery)) {
            return SYMBOL_CONTAINS_SCORE;
        }

        if (normalizedName.contains(normalizedQuery)) {
            return NAME_CONTAINS_SCORE;
        }

        return NO_MATCH_SCORE;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return value
                .trim()
                .toUpperCase(Locale.ROOT);
    }
}