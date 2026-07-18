/*
 * Project : Stock Analytics Platform
 *
 * Description:
 * REST controller exposing instrument search endpoints.
 */

package com.stockanalytics.controller;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.service.InstrumentSearchService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/instruments")
@Validated
public class InstrumentSearchController {

    private final InstrumentSearchService instrumentSearchService;

    public InstrumentSearchController(
            InstrumentSearchService instrumentSearchService) {

        this.instrumentSearchService = Objects.requireNonNull(
                instrumentSearchService,
                "instrumentSearchService must not be null");
    }

    /**
     * Searches instruments by company name or symbol.
     *
     * Examples:
     *
     * /api/v1/instruments/search?q=reliance
     * /api/v1/instruments/search?q=tcs
     */
    @GetMapping("/search")
    public List<InstrumentSearchResult> search(
            @RequestParam("q")
            @NotBlank(message = "Search query must not be blank")
            String query) {

        return instrumentSearchService.search(query);
    }
}