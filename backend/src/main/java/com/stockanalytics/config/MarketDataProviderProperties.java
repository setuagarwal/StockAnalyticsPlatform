/*
 * Project : Stock Analytics Platform
 *
 * Copyright (c) 2026.
 *
 * Description:
 * Loads market-data provider ordering from application configuration.
 */

package com.stockanalytics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "market-data")
public class MarketDataProviderProperties {

    private ProviderOrder providerOrder = new ProviderOrder();

    public ProviderOrder getProviderOrder() {
        return providerOrder;
    }

    public void setProviderOrder(ProviderOrder providerOrder) {
        this.providerOrder = providerOrder;
    }

    public static class ProviderOrder {

        private List<String> instrumentSearch = new ArrayList<>();

        public List<String> getInstrumentSearch() {
            return List.copyOf(instrumentSearch);
        }

        public void setInstrumentSearch(List<String> instrumentSearch) {
            this.instrumentSearch = instrumentSearch == null
                    ? new ArrayList<>()
                    : new ArrayList<>(instrumentSearch);
        }
    }
}