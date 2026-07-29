package com.stockanalytics.service.search;

import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Provides consistent normalization for instrument search queries
 * and searchable instrument text.
 */
@Component
public class QueryNormalizer {

    /**
     * Normalizes text for case-insensitive and punctuation-tolerant matching.
     *
     * Normalization rules:
     * - null values become an empty string
     * - leading and trailing spaces are removed
     * - text is converted to upper case
     * - punctuation is replaced with spaces
     * - repeated spaces are collapsed
     */
    public String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return value
                .trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
