/*
 * Project : Stock Analytics Platform
 *
 * Copyright (c) 2026.
 *
 * Description:
 * Indicates that none of the configured market-data providers
 * could successfully complete the requested operation.
 */

package com.stockanalytics.exception;

public class MarketDataUnavailableException extends RuntimeException {

    public MarketDataUnavailableException(String message) {
        super(message);
    }

    public MarketDataUnavailableException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}