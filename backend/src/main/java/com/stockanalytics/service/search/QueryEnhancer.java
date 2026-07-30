package com.stockanalytics.service.search;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Applies generic, provider-independent enhancements to instrument
 * search queries.
 *
 * This component intentionally avoids company-specific aliases.
 */
@Component
public class QueryEnhancer {

    private static final Set<String> TRAILING_COMPANY_SUFFIXES = Set.of(
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

    private final QueryNormalizer queryNormalizer;

    public QueryEnhancer(QueryNormalizer queryNormalizer) {
        this.queryNormalizer = queryNormalizer;
    }

    /**
     * Enhances a user-entered search query using generic rules.
     *
     * Rules:
     * - converts '&' to 'AND' before normalization
     * - applies the shared query normalizer
     * - removes trailing legal company suffixes
     */
    public String enhance(String query) {
        if (query == null || query.isBlank()) {
            return "";
        }

        String queryWithNormalizedConnectors =
                query.replace("&", " AND ");

        String normalizedQuery =
                queryNormalizer.normalize(queryWithNormalizedConnectors);

        if (normalizedQuery.isEmpty()) {
            return "";
        }

        List<String> words = new ArrayList<>(
                List.of(normalizedQuery.split(" "))
        );

        while (!words.isEmpty()
                && TRAILING_COMPANY_SUFFIXES.contains(words.getLast())) {
            words.removeLast();
        }

        return String.join(" ", words).trim();
    }
}
