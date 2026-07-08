# Stock Analytics Platform - External Providers

## Purpose

This document defines the external services used by the Stock Analytics Platform and the abstraction layer used to integrate them.

The application must never call third-party providers directly from the UI. All integrations are performed through the Spring Boot backend.

---

# Design Principles

- Provider independent
- Easy to replace providers
- Support fallback providers
- Keep provider-specific logic isolated
- Minimise infrastructure cost
- Prefer free APIs where practical

---

# Provider Categories

| Category | Purpose |
|----------|---------|
| Market Data | Search instruments, quotes, historical candles |
| News | Company, sector and market news |
| AI | Technical summaries and news summarisation |
| Notifications | Desktop, Email and WhatsApp alerts |

---

# Market Data Provider

## Purpose

Provide:

- Instrument search
- Current quote
- Historical OHLCV data

## Initial Strategy

Primary provider:
- Yahoo Finance (unofficial)

Fallback providers:
- Alpha Vantage (free tier)
- Twelve Data (free tier)

The backend exposes a common interface regardless of the provider.

```text
MarketDataProvider
    |
    +-- YahooFinanceProvider
    +-- AlphaVantageProvider
    +-- TwelveDataProvider
```

Provider selection should be configurable.

---

# News Provider

## Purpose

Provide:

- Company news
- Sector news
- General market news

Preferred implementation:

- NewsAPI (free tier where applicable)
- Alternative providers can be added later.

```text
NewsProvider
    |
    +-- NewsApiProvider
```

---

# AI Provider

## Purpose

Generate:

- Technical summaries
- Trend observations
- News summaries
- Risk observations

The backend communicates through an abstraction.

```text
AiProvider
    |
    +-- OpenAIProvider
```

The implementation should allow additional AI providers in the future.

---

# Notification Providers

Supported channels:

- Desktop notification
- Email
- WhatsApp

```text
NotificationProvider
    |
    +-- DesktopNotificationProvider
    +-- EmailNotificationProvider
    +-- WhatsAppNotificationProvider
```

Desktop notifications should work without any external service.

---

# Configuration

Each provider should support:

- Enabled / Disabled
- API Key
- Base URL
- Timeout
- Retry count

Configuration should be external to source code.

---

# Provider Failover

General flow:

```text
Primary Provider
       |
Success? ---- Yes --> Return Response
       |
      No
       |
Fallback Provider
       |
Return Response / Error
```

If no fallback exists, return a user-friendly error.

---

# Rate Limits

The application should:

- Cache short-lived responses where appropriate
- Avoid duplicate requests
- Respect provider limits
- Retry transient failures with backoff

---

# Error Handling

Common provider errors:

- Authentication failure
- Network timeout
- Rate limit exceeded
- Invalid response
- Service unavailable

These should be translated into consistent backend error responses.

---

# Acceptance Criteria

- UI never calls providers directly.
- Providers are accessed only through backend interfaces.
- Providers can be replaced with minimal code changes.
- Configuration is externalised.
- Provider failures are handled gracefully.
