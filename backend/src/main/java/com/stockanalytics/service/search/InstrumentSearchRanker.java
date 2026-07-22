package com.stockanalytics.service.search;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Orders instrument search results by relevance to the user's query.
 *
 * Higher-scoring results appear first. Java's stream sorting is stable,
 * so results with the same score retain their original provider order.
 */
@Component
public class InstrumentSearchRanker {

    private final InstrumentSearchScorer scorer;

    public InstrumentSearchRanker(InstrumentSearchScorer scorer) {
        this.scorer = scorer;
    }

    public List<InstrumentSearchResult> rank(
            String query,
            List<InstrumentSearchResult> results
    ) {
        if (results == null || results.isEmpty()) {
            return List.of();
        }

        return results.stream()
                .filter(result -> result != null)
                .sorted(
                        Comparator.comparingInt(
                                (InstrumentSearchResult result) ->
                                        scorer.calculateScore(query, result)
                        ).reversed()
                )
                .toList();
    }
}