package com.stockanalytics.dto;

import java.util.Objects;

/**
 * Represents a single instrument returned by the instrument search API.
 *
 * This DTO is sent to the frontend and contains only the information
 * required to display search results.
 */
public final class InstrumentSearchResult {

    private final String exchange;
    private final String symbol;
    private final String companyName;
    private final String instrumentType;

    public InstrumentSearchResult(
            String exchange,
            String symbol,
            String companyName,
            String instrumentType) {

        this.exchange = Objects.requireNonNull(exchange, "exchange must not be null");
        this.symbol = Objects.requireNonNull(symbol, "symbol must not be null");
        this.companyName = Objects.requireNonNull(companyName, "companyName must not be null");
        this.instrumentType = Objects.requireNonNull(instrumentType, "instrumentType must not be null");
    }

    public String getExchange() {
        return exchange;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    @Override
    public String toString() {
        return "InstrumentSearchResult{" +
                "exchange='" + exchange + '\'' +
                ", symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", instrumentType='" + instrumentType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof InstrumentSearchResult other)) {
            return false;
        }

        return exchange.equals(other.exchange)
                && symbol.equals(other.symbol)
                && companyName.equals(other.companyName)
                && instrumentType.equals(other.instrumentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchange, symbol, companyName, instrumentType);
    }
}