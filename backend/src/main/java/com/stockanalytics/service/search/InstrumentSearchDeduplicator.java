package com.stockanalytics.service.search;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Removes duplicate instruments from search results while preserving
 * the original result order.
 *
 * Instruments are currently considered duplicates when they have the
 * same normalized symbol and exchange.
 */
@Component
public class InstrumentSearchDeduplicator {

    public List<InstrumentSearchResult> removeDuplicates(
            List<InstrumentSearchResult> results
    ) {
        if (results == null || results.isEmpty()) {
            return List.of();
        }

        Set<String> seenKeys = new HashSet<>();
        List<InstrumentSearchResult> uniqueResults = new ArrayList<>();

        for (InstrumentSearchResult result : results) {
            if (result == null) {
                continue;
            }

            String key = createKey(result);

            if (seenKeys.add(key)) {
                uniqueResults.add(result);
            }
        }

        return List.copyOf(uniqueResults);
    }

    private String createKey(InstrumentSearchResult result) {
        String normalizedSymbol = normalize(result.symbol());
        String normalizedExchange = result.exchange() == null
                ? "UNKNOWN"
                : result.exchange().name();

        return normalizedExchange + ":" + normalizedSymbol;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return value.trim().toUpperCase(Locale.ROOT);
    }
}