package com.stockanalytics.dto.response;

import com.stockanalytics.domain.Country;
import com.stockanalytics.domain.Exchange;
import com.stockanalytics.domain.InstrumentType;

/**
 * Provider-independent instrument search result returned by the API.
 *
 * Provider-specific codes must be normalized before creating this response.
 */
public record InstrumentSearchResult(
        Exchange exchange,
        String symbol,
        String name,
        InstrumentType instrumentType,
        Country country
) {
}