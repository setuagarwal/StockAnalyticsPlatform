package com.stockanalytics.controller;

import com.stockanalytics.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class HealthController {

    @GetMapping("/api/v1/health")
    public HealthResponse health() {
        return new HealthResponse("UP", "Stock Analytics Backend", OffsetDateTime.now());
    }
}
