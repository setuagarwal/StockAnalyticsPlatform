/*
 * Project : Stock Analytics Platform
 *
 * Description:
 * REST controller exposing instrument search endpoints.
 */

package com.stockanalytics.controller;

import com.stockanalytics.dto.response.InstrumentSearchResult;
import com.stockanalytics.provider.ProviderResult;
import com.stockanalytics.service.InstrumentSearchService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                "instrumentSearchService must not be null"
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ProviderResult<List<InstrumentSearchResult>>> search(
            @RequestParam(name = "query")
            @NotBlank(message = "Search query must not be blank")
            String query) {

        ProviderResult<List<InstrumentSearchResult>> result =
                instrumentSearchService.search(query);

        return ResponseEntity.ok(result);
    }
}