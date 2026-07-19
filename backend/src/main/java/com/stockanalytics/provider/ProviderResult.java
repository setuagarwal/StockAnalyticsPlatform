/*
 * Project : Stock Analytics Platform
 *
 * Copyright (c) 2026.
 *
 * Description:
 * Represents successful data returned by a provider, together with
 * information identifying the provider that supplied the data.
 */

package com.stockanalytics.provider;

import java.util.Objects;

public final class ProviderResult<T> {

    private final T data;
    private final String providerCode;
    private final String providerDisplayName;

    public ProviderResult(
            T data,
            String providerCode,
            String providerDisplayName) {

        this.data = Objects.requireNonNull(
                data,
                "data must not be null"
        );

        this.providerCode = requireNonBlank(
                providerCode,
                "providerCode must not be blank"
        );

        this.providerDisplayName = requireNonBlank(
                providerDisplayName,
                "providerDisplayName must not be blank"
        );
    }

    public T getData() {
        return data;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public String getProviderDisplayName() {
        return providerDisplayName;
    }

    private static String requireNonBlank(
            String value,
            String message) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    @Override
    public String toString() {
        return "ProviderResult{" +
                "data=" + data +
                ", providerCode='" + providerCode + '\'' +
                ", providerDisplayName='" + providerDisplayName + '\'' +
                '}';
    }
}