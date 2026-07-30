package com.stockanalytics.service;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.provider.market.MarketDataProviderManager;
import com.stockanalytics.service.search.InstrumentSearchDeduplicator;
import com.stockanalytics.service.search.InstrumentSearchRanker;
import com.stockanalytics.service.search.QueryEnhancer;
import org.springframework.stereotype.Service;
import com.stockanalytics.service.search.InstrumentSearchGrouper;

import java.util.List;

@Service
public class InstrumentSearchService {

    private final MarketDataProviderManager providerManager;
    private final InstrumentSearchDeduplicator deduplicator;
    private final InstrumentSearchRanker ranker;
    private final InstrumentSearchGrouper grouper;
    private final QueryEnhancer queryEnhancer;

    public InstrumentSearchService(
            MarketDataProviderManager providerManager,
            InstrumentSearchDeduplicator deduplicator,
            InstrumentSearchRanker ranker,
            InstrumentSearchGrouper grouper,
            QueryEnhancer queryEnhancer
    ) {
        this.providerManager = providerManager;
        this.deduplicator = deduplicator;
        this.ranker = ranker;
        this.grouper = grouper;
        this.queryEnhancer = queryEnhancer;
    }

    public ProviderResult<List<InstrumentSearchResult>> search(String query) {
        String enhancedQuery = queryEnhancer.enhance(query);

        ProviderResult<List<InstrumentSearchResult>> providerResult =
                providerManager.searchInstruments(enhancedQuery);

        List<InstrumentSearchResult> uniqueResults =
                deduplicator.removeDuplicates(providerResult.getData());

        List<InstrumentSearchResult> rankedResults =
                ranker.rank(enhancedQuery, uniqueResults);
				
		List<InstrumentSearchResult> groupedResults =
				grouper.group(rankedResults);		

        return new ProviderResult<>(
                groupedResults,
                providerResult.getProviderCode(),
                providerResult.getProviderDisplayName()
        );
    }
}