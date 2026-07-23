package com.stockanalytics.service;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.provider.market.MarketDataProviderManager;
import com.stockanalytics.service.search.InstrumentSearchDeduplicator;
import com.stockanalytics.service.search.InstrumentSearchRanker;
import org.springframework.stereotype.Service;
import com.stockanalytics.service.search.InstrumentSearchGrouper;

import java.util.List;

@Service
public class InstrumentSearchService {

    private final MarketDataProviderManager providerManager;
    private final InstrumentSearchDeduplicator deduplicator;
    private final InstrumentSearchRanker ranker;
	private final InstrumentSearchGrouper grouper;

    public InstrumentSearchService(
            MarketDataProviderManager providerManager,
            InstrumentSearchDeduplicator deduplicator,
            InstrumentSearchRanker ranker,
			InstrumentSearchGrouper grouper
    ) {
        this.providerManager = providerManager;
        this.deduplicator = deduplicator;
        this.ranker = ranker;
		this.grouper = grouper;
    }

    public ProviderResult<List<InstrumentSearchResult>> search(String query) {
        ProviderResult<List<InstrumentSearchResult>> providerResult =
                providerManager.searchInstruments(query);

        List<InstrumentSearchResult> uniqueResults =
                deduplicator.removeDuplicates(providerResult.getData());

        List<InstrumentSearchResult> rankedResults =
                ranker.rank(query, uniqueResults);
				
		List<InstrumentSearchResult> groupedResults =
				grouper.group(rankedResults);		

        return new ProviderResult<>(
                groupedResults,
                providerResult.getProviderCode(),
                providerResult.getProviderDisplayName()
        );
    }
}