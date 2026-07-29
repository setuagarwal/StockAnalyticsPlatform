package com.stockanalytics.service.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InstrumentSearchMetricsLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    InstrumentSearchMetricsLogger.class
            );

    public void log(InstrumentSearchMetrics metrics) {
        LOGGER.info(
                "Instrument search completed: " +
                        "query={}, provider={}, resultCount={}, " +
                        "durationMs={}, fallbackUsed={}",
                metrics.query(),
                metrics.providerCode(),
                metrics.resultCount(),
                metrics.durationMilliseconds(),
                metrics.fallbackUsed()
        );
    }
}