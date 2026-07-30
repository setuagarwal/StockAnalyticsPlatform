package com.stockanalytics.domain.market;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoricalPricePoint(

        LocalDate date,

        BigDecimal open,

        BigDecimal high,

        BigDecimal low,

        BigDecimal close,

        BigDecimal adjustedClose,

        Long volume

) {
}