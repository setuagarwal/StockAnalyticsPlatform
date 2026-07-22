package com.stockanalytics.config.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "http.client")
public class WebClientProperties {

    private int connectTimeoutSeconds = 5;
    private int responseTimeoutSeconds = 10;

    public int getConnectTimeoutSeconds() {
        return connectTimeoutSeconds;
    }

    public void setConnectTimeoutSeconds(int connectTimeoutSeconds) {
        this.connectTimeoutSeconds = connectTimeoutSeconds;
    }

    public int getResponseTimeoutSeconds() {
        return responseTimeoutSeconds;
    }

    public void setResponseTimeoutSeconds(int responseTimeoutSeconds) {
        this.responseTimeoutSeconds = responseTimeoutSeconds;
    }
}