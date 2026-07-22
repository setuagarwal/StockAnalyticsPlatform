package com.stockanalytics.provider.market.yahoo.mapper;

import com.stockanalytics.domain.Country;
import com.stockanalytics.domain.Exchange;
import com.stockanalytics.domain.InstrumentType;
import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.provider.market.yahoo.dto.YahooSearchResponse;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class YahooInstrumentMapper {

    public InstrumentSearchResult toInstrumentSearchResult(
            YahooSearchResponse.YahooQuote quote) {

        Exchange exchange = mapExchange(quote.exchange());

        return new InstrumentSearchResult(
                exchange,
                quote.symbol(),
                resolveName(quote),
                mapInstrumentType(quote.quoteType()),
                mapCountry(exchange)
        );
    }

    private String resolveName(YahooSearchResponse.YahooQuote quote) {
        if (quote.longname() != null && !quote.longname().isBlank()) {
            return quote.longname();
        }

        if (quote.shortname() != null && !quote.shortname().isBlank()) {
            return quote.shortname();
        }

        return quote.symbol();
    }

    private Exchange mapExchange(String yahooExchangeCode) {
        if (yahooExchangeCode == null || yahooExchangeCode.isBlank()) {
            return Exchange.UNKNOWN;
        }

        return switch (yahooExchangeCode.trim().toUpperCase(Locale.ROOT)) {
            case "NSI" -> Exchange.NSE;
            case "BSE" -> Exchange.BSE;
            case "NYQ" -> Exchange.NYSE;
            case "NMS", "NGM", "NCM" -> Exchange.NASDAQ;
            case "LSE" -> Exchange.LSE;
            case "FRA" -> Exchange.FRANKFURT;
            default -> Exchange.UNKNOWN;
        };
    }

    private InstrumentType mapInstrumentType(String yahooQuoteType) {
        if (yahooQuoteType == null || yahooQuoteType.isBlank()) {
            return InstrumentType.UNKNOWN;
        }

        return switch (yahooQuoteType.trim().toUpperCase(Locale.ROOT)) {
            case "EQUITY" -> InstrumentType.EQUITY;
            case "ETF" -> InstrumentType.ETF;
            case "MUTUALFUND", "MUTUAL_FUND" ->
                    InstrumentType.MUTUAL_FUND;
            case "INDEX" -> InstrumentType.INDEX;
            case "CURRENCY" -> InstrumentType.CURRENCY;
            case "CRYPTOCURRENCY", "CRYPTO" ->
                    InstrumentType.CRYPTOCURRENCY;
            case "FUTURE" -> InstrumentType.FUTURE;
            case "OPTION" -> InstrumentType.OPTION;
            case "BOND" -> InstrumentType.BOND;
            case "WARRANT" -> InstrumentType.WARRANT;
            default -> InstrumentType.UNKNOWN;
        };
    }

    private Country mapCountry(Exchange exchange) {
        if (exchange == null) {
            return Country.UNKNOWN;
        }

        return switch (exchange) {
            case NSE, BSE -> Country.INDIA;
            case NYSE, NASDAQ -> Country.UNITED_STATES;
            case LSE -> Country.UNITED_KINGDOM;
            case FRANKFURT -> Country.GERMANY;
            case UNKNOWN -> Country.UNKNOWN;
        };
    }
}