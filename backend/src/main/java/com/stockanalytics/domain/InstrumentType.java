package com.stockanalytics.domain;

/**
 * Provider-independent classification of financial instruments.
 *
 * External provider values must be converted to this enum inside
 * the relevant provider mapper.
 */
public enum InstrumentType {

    EQUITY,
    ETF,
    MUTUAL_FUND,
    INDEX,
    CURRENCY,
    CRYPTOCURRENCY,
    FUTURE,
    OPTION,
    BOND,
    WARRANT,

    /**
     * Used when a provider returns an instrument type that the
     * application does not currently recognise.
     */
    UNKNOWN
}