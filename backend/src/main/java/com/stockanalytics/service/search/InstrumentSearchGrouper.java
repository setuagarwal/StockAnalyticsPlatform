package com.stockanalytics.service.search;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Keeps listings of the same company and country together while
 * preserving the order established by the search ranker.
 *
 * NSE and BSE listings of the same Indian company remain together,
 * while similarly named foreign listings remain separate.
 */
@Component
public class InstrumentSearchGrouper {

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

    public List<InstrumentSearchResult> group(
            List<InstrumentSearchResult> results
    ) {
        if (results == null || results.isEmpty()) {
            return List.of();
        }

        Map<String, List<InstrumentSearchResult>> groupedResults =
                new LinkedHashMap<>();

        for (InstrumentSearchResult result : results) {
            if (result == null) {
                continue;
            }

            String groupingKey = createGroupingKey(result);

            groupedResults
                    .computeIfAbsent(
                            groupingKey,
                            ignored -> new ArrayList<>()
                    )
                    .add(result);
        }

        return groupedResults.values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    private String createGroupingKey(
            InstrumentSearchResult result
    ) {
        String countryKey = result.country() == null
                ? "UNKNOWN"
                : result.country().name();

        String normalizedName =
                normalizeCompanyName(result.name());

        if (!normalizedName.isEmpty()) {
            return countryKey + ":" + normalizedName;
        }

        /*
         * Symbol is used only when the company name is missing.
         */
        return countryKey + ":" + normalizeSymbol(result.symbol());
    }

    private String normalizeCompanyName(String companyName) {
        if (companyName == null || companyName.isBlank()) {
            return "";
        }

        String normalized = companyName
                .trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9\\s]", " ")
                .replaceAll("\\s+", " ");

        List<String> words = new ArrayList<>(
                List.of(normalized.split(" "))
        );

        while (!words.isEmpty()
                && COMPANY_SUFFIXES.contains(words.getLast())) {
            words.removeLast();
        }

        return String.join(" ", words).trim();
    }

    private String normalizeSymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            return "";
        }

        return symbol
                .trim()
                .toUpperCase(Locale.ROOT)
                .replaceFirst("\\.(NS|BO|NSE|BSE)$", "");
    }
}