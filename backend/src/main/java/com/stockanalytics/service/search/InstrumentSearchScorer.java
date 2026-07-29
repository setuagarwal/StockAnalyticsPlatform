package com.stockanalytics.service.search;

import com.stockanalytics.config.search.SearchPreferenceProperties;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    private static final int EXACT_NORMALIZED_COMPANY_NAME_SCORE = 88;
    private static final int EXACT_NAME_SCORE = 85;
    private static final int NAME_STARTS_WITH_SCORE = 75;
    private static final int SYMBOL_CONTAINS_SCORE = 65;
    private static final int NAME_CONTAINS_SCORE = 55;
    private static final int NO_MATCH_SCORE = 0;

    private static final Set<String> COMPANY_SUFFIXES = Set.of(
            "LIMITED",
            "LTD",
            "INC",
            "INCORPORATED",
            "CORPORATION",
            "CORP",
            "PLC",
            "LLC",
            "LP",
            "SA",
            "AG",
            "NV"
    );

    private final SearchPreferenceProperties searchPreferenceProperties;
	
	private final QueryNormalizer queryNormalizer;

    public InstrumentSearchScorer(
            SearchPreferenceProperties searchPreferenceProperties,
			QueryNormalizer queryNormalizer
    ) {
        this.searchPreferenceProperties = searchPreferenceProperties;
		this.queryNormalizer = queryNormalizer;
    }

    public int calculateScore(
            String query,
            InstrumentSearchResult result
    ) {
        if (result == null) {
            return NO_MATCH_SCORE;
        }

        String normalizedQuery = queryNormalizer.normalize(query);

        if (normalizedQuery.isEmpty()) {
            return NO_MATCH_SCORE;
        }

        String normalizedSymbol = queryNormalizer.normalize(result.symbol());
        String normalizedName = queryNormalizer.normalize(result.name());
        String normalizedCompanyName =
                normalizeCompanyName(result.name());

        int relevanceScore = calculateRelevanceScore(
                normalizedQuery,
                normalizedSymbol,
                normalizedName,
                normalizedCompanyName
        );

        int exchangePreferenceScore =
                searchPreferenceProperties.getExchangePreferenceScore(
                        result.exchange()
                );

        int instrumentTypePreferenceScore =
                searchPreferenceProperties
                        .getInstrumentTypePreferenceScore(
                                result.instrumentType()
                        );

        return relevanceScore
                + exchangePreferenceScore
                + instrumentTypePreferenceScore;
    }

    private int calculateRelevanceScore(
            String normalizedQuery,
            String normalizedSymbol,
            String normalizedName,
            String normalizedCompanyName
    ) {
        if (normalizedSymbol.equals(normalizedQuery)) {
            return EXACT_SYMBOL_SCORE;
        }

        if (normalizedSymbol.startsWith(normalizedQuery)) {
            return SYMBOL_STARTS_WITH_SCORE;
        }

        if (normalizedCompanyName.equals(normalizedQuery)) {
            return EXACT_NORMALIZED_COMPANY_NAME_SCORE;
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

    private String normalizeCompanyName(String companyName) {
        String normalized = queryNormalizer.normalize(companyName);

        if (normalized.isEmpty()) {
            return "";
        }

        List<String> words = new ArrayList<>(
                List.of(normalized.split(" "))
        );

        while (!words.isEmpty()
                && COMPANY_SUFFIXES.contains(words.getLast())) {
            words.removeLast();
        }

        return String.join(" ", words).trim();
    }
}