package com.stockanalytics.service.search;

public record InstrumentSearchMetrics(
        String query,
        String providerCode,
        int resultCount,
        long durationMilliseconds,
        boolean fallbackUsed
) {
}