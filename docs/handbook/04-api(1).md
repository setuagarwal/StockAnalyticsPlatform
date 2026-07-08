# Stock Analytics Platform - API Specification

## Purpose

Defines the Version 1 REST APIs.

Base URL

```
/api/v1
```

---

# API Standards

- REST
- JSON
- Stateless
- UTF-8
- Versioned endpoints

---

# Instrument Search

## Search Instruments

**GET**

```
/api/v1/instruments/search?q={query}
```

Returns matching NSE and BSE instruments.

---

# Chart APIs

## Historical Data

**GET**

```
/api/v1/charts/{exchange}/{symbol}
```

Query Parameters

- timeframe
- from
- to

---

# Indicator APIs

## List Indicators

**GET**

```
/api/v1/indicators
```

## Calculate Indicators

**POST**

```
/api/v1/indicators/calculate
```

---

# Drawing APIs

## Save Drawings

**POST**

```
/api/v1/drawings
```

## Load Drawings

**GET**

```
/api/v1/drawings/{workspaceId}
```

---

# AI APIs

## Analyse Instrument

**POST**

```
/api/v1/ai/analyse
```

Returns:

- Trend summary
- Indicator summary
- Risk observations
- Support / Resistance
- Confidence score

---

# News APIs

## Company News

**GET**

```
/api/v1/news/company
```

## Sector News

**GET**

```
/api/v1/news/sector
```

---

# Position & Target Tracking APIs

## Get Tracking

**GET**

```
/api/v1/tracking/{exchange}/{symbol}
```

Returns current tracking information for the instrument.

---

## Save Tracking

**POST**

```
/api/v1/tracking
```

Example

```json
{
  "exchange":"NSE",
  "symbol":"RELIANCE",
  "trackingType":"OWNED",
  "averageBuyPrice":2450.50,
  "quantity":100,
  "targetSellPrice":2850,
  "stopLossPrice":2300,
  "notes":"Long-term investment"
}
```

For WATCH mode:

```json
{
  "exchange":"NSE",
  "symbol":"TCS",
  "trackingType":"WATCH",
  "targetBuyPrice":3200,
  "targetBuyRangeLow":3180,
  "targetBuyRangeHigh":3220,
  "notes":"Buy near support"
}
```

---

## Update Tracking

**PUT**

```
/api/v1/tracking/{id}
```

---

## Delete Tracking

**DELETE**

```
/api/v1/tracking/{id}
```

---

## List Tracked Stocks

**GET**

```
/api/v1/tracking
```

Optional filters:

- trackingType=OWNED
- trackingType=WATCH

---

# Alert APIs

## Create Alert

**POST**

```
/api/v1/alerts
```

## Update Alert

**PUT**

```
/api/v1/alerts/{id}
```

## Delete Alert

**DELETE**

```
/api/v1/alerts/{id}
```

## List Alerts

**GET**

```
/api/v1/alerts
```

Supported alert types:

- Price
- Buy Target
- Sell Target
- Stop-loss
- Indicator
- Pattern
- Volume

---

# Workspace APIs

GET /api/v1/workspaces

POST /api/v1/workspaces

PUT /api/v1/workspaces/{id}

DELETE /api/v1/workspaces/{id}

---

# Notes APIs

POST /api/v1/notes

GET /api/v1/notes/{workspaceId}

---

# Standard Error Response

```json
{
  "timestamp":"2026-07-08T10:15:00Z",
  "status":400,
  "error":"Bad Request",
  "message":"Validation failed",
  "path":"/api/v1/tracking"
}
```

---

# Acceptance Criteria

Version 1 APIs support:

- Instrument Search
- Charts
- Indicators
- Drawings
- AI Analysis
- News
- Position & Target Tracking
- Alerts
- Notes
- Workspaces
