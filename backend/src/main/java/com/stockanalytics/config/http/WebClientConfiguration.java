package com.stockanalytics.config.http;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient.Builder webClientBuilder(
            WebClientProperties webClientProperties) {

        Objects.requireNonNull(
                webClientProperties,
                "webClientProperties must not be null"
        );

        int connectTimeoutSeconds =
                requirePositive(
                        webClientProperties.getConnectTimeoutSeconds(),
                        "Connect timeout must be greater than zero"
                );

        int responseTimeoutSeconds =
                requirePositive(
                        webClientProperties.getResponseTimeoutSeconds(),
                        "Response timeout must be greater than zero"
                );

        HttpClient httpClient = HttpClient.create()
                .option(
                        ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        Math.multiplyExact(connectTimeoutSeconds, 1000)
                )
                .responseTimeout(
                        Duration.ofSeconds(responseTimeoutSeconds)
                )
                .doOnConnected(connection ->
                        connection
                                .addHandlerLast(
                                        new ReadTimeoutHandler(
                                                responseTimeoutSeconds,
                                                TimeUnit.SECONDS
                                        )
                                )
                                .addHandlerLast(
                                        new WriteTimeoutHandler(
                                                responseTimeoutSeconds,
                                                TimeUnit.SECONDS
                                        )
                                )
                );

        return WebClient.builder()
                .clientConnector(
                        new ReactorClientHttpConnector(httpClient)
                );
    }

    private int requirePositive(
            int value,
            String message) {

        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }
}