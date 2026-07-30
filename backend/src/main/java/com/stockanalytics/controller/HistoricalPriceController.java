package com.stockanalytics.controller;

import com.stockanalytics.domain.market.HistoricalPricePoint;
import com.stockanalytics.domain.market.HistoricalRange;
import com.stockanalytics.domain.market.MarketDataInterval;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.service.HistoricalPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/instruments")
public class HistoricalPriceController {

    private final HistoricalPriceService historicalPriceService;

    public HistoricalPriceController(
            HistoricalPriceService historicalPriceService) {

        this.historicalPriceService = Objects.requireNonNull(
                historicalPriceService,
                "historicalPriceService must not be null"
        );
    }

    @GetMapping("/{symbol}/historical-prices")
	public ResponseEntity<
        ProviderResult<List<HistoricalPricePoint>>
        > getHistoricalPrices(

        @PathVariable("symbol")
        String symbol,

        @RequestParam(
                name = "range",
                defaultValue = "ONE_MONTH"
        )
        HistoricalRange range,

        @RequestParam(
                name = "interval",
                defaultValue = "ONE_DAY"
        )
        MarketDataInterval interval
) {
    ProviderResult<List<HistoricalPricePoint>> response =
            historicalPriceService.getHistoricalPrices(
                    symbol,
                    range,
                    interval
            );

    return ResponseEntity.ok(response);
}
}