---
title: Project Specification
document: 01-project
version: 1.1
status: Draft
owner: Setu Agarwal
last_updated: 2026-07-08
---

# Stock Analytics Platform

## 1. Purpose

The Stock Analytics Platform is a personal desktop application for analysing stocks listed on the National Stock Exchange (NSE) and Bombay Stock Exchange (BSE). It combines charting, technical analysis, AI-assisted insights, news analysis and research into a single desktop workspace.

---

## 2. Why This Project Exists

The objective is to build a professional-grade stock analysis application that is simple to maintain, extensible and inexpensive to operate. The application is intended for personal use and will rely primarily on real-time market data instead of maintaining a large local market database.

---

## 3. Objectives

The application should enable the user to:

- Search stocks across NSE and BSE.
- Analyse charts using technical indicators.
- Draw and annotate charts.
- View company, sector and market news.
- Generate AI-assisted technical summaries.
- Save workspaces and notes.
- Configure alerts.
- Track owned and watched positions.
- Extend functionality through plugins.

---

## 4. Version 1 Scope

### Included

- NSE & BSE equities
- Interactive charts
- Multiple timeframes
- Technical indicators
- Drawing tools
- AI insights
- News
- Alerts
- Notes
- Workspaces
- Position & Target Tracking
- Plugin-ready architecture

### Excluded

- Trade execution
- Broker integration
- Automated trading
- Portfolio management
- Asset allocation
- Taxation
- Mobile application
- Cloud synchronisation

---

## 5. Position & Target Tracking

The application allows the user to optionally record tracking information for any analysed stock.

### Owned Position

The user may record:

- Average buying price
- Quantity owned
- Target selling price (optional)
- Stop-loss price (optional)
- Personal notes (optional)

The application will:

- Display this information whenever the stock is opened.
- Monitor market price against the configured target selling price and stop-loss.
- Generate alerts when configured conditions are met.

### Watch Position

The user may record:

- Target buying price
- Target buying price range (optional)
- Target stop-loss after purchase (optional)
- Personal notes

The application will:

- Monitor market prices.
- Notify the user when the stock reaches the configured buying price or range.
- Display the configured buying target whenever the stock is opened.

### Untracked

Default state.

The stock is analysed without storing any tracking information.

The application will not calculate:

- Portfolio value
- Profit & Loss
- Asset allocation
- Taxation
- Broker reconciliation
- Order history

---

## 6. High-Level Solution

```text
React UI
    |
REST API
    |
Spring Boot Backend
    |
Business Services
(Search, Charts, Indicators, AI, News, Alerts, Tracking, Workspaces)
    |
Repository Layer
    |
SQLite

External Providers
- Market Data
- News
- AI
```

---

## 7. Technology Stack

- React + TypeScript + Material UI
- Java 21 + Spring Boot
- SQLite
- TradingView Lightweight Charts
- Maven + npm
- Git + GitHub

---

## 8. Development Workflow

1. Update documentation.
2. Generate implementation.
3. Review generated code.
4. Approve implementation.
5. Commit to Git.

No AI-generated code is added to the codebase without manual approval.

---

## 9. Guiding Principles

- Simplicity
- Functionality
- Maintainability
- Extensibility
- Incremental development
- Local-first storage
- Real-time market data

---

## 10. References

- README.md
- 02-architecture.md
- 03-ui.md
- 04-api.md
- 05-database.md
