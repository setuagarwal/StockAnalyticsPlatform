package com.stockanalytics.config.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "market-data.providers.yahoo")
public class YahooProviderProperties {

    /**
     * Controls whether the Yahoo provider is available.
     */
    private boolean enabled = true;

    /**
     * Example: https://query1.finance.yahoo.com
     */
    private String baseUrl;

    /**
     * Response timeout in seconds.
     */
    private int timeoutSeconds = 10;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}