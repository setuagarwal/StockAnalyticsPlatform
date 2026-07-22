package com.stockanalytics.domain;

/**
 * Provider-independent representation of the country associated
 * with an instrument's primary exchange.
 *
 * Provider-specific values should be translated into this enum
 * inside the relevant provider mapper.
 */
public enum Country {

    INDIA,
    UNITED_STATES,
    UNITED_KINGDOM,
    GERMANY,
    JAPAN,
    CANADA,
    AUSTRALIA,

    UNKNOWN
}