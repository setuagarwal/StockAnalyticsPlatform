package com.stockanalytics.domain;

/**
 * Provider-independent representation of a financial exchange.
 *
 * Provider-specific exchange codes must be translated into this enum
 * inside the relevant provider mapper.
 */
public enum Exchange {

    NSE,
    BSE,

    NYSE,
    NASDAQ,

    LSE,
    FRANKFURT,

    UNKNOWN
}