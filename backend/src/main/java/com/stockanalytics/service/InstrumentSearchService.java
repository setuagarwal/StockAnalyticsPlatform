package com.stockanalytics.service;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.provider.market.MarketDataProviderManager;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.service.search.InstrumentSearchDeduplicator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentSearchService {

    private final MarketDataProviderManager providerManager;
    private final InstrumentSearchDeduplicator deduplicator;

    public InstrumentSearchService(
            MarketDataProviderManager providerManager,
            InstrumentSearchDeduplicator deduplicator
    ) {
        this.providerManager = providerManager;
        this.deduplicator = deduplicator;
    }

    public ProviderResult<List<InstrumentSearchResult>> search(String query) {
        ProviderResult<List<InstrumentSearchResult>> providerResult =
                providerManager.searchInstruments(query);

        List<InstrumentSearchResult> uniqueResults =
                deduplicator.removeDuplicates(providerResult.getData());

        return new ProviderResult<>(
                uniqueResults,
                providerResult.getProviderCode(),
                providerResult.getProviderDisplayName()
        );
    }
}