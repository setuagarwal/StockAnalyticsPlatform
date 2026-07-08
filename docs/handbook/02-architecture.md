# Stock Analytics Platform - Architecture

## Purpose

This document defines the high-level architecture for Version 1 of the Stock Analytics Platform.

The application is a personal desktop solution focused on stock analysis, position & target tracking, AI-assisted insights and extensibility.

---

# High-Level Architecture

```text
+----------------------+
|   React Frontend     |
+----------------------+
           |
           | REST API
           v
+----------------------+
| Spring Boot Backend  |
+----------------------+
           |
           +------------------------------------------------------+
           |        |         |         |         |               |
           v        v         v         v         v               v
      Search   Charts   Indicators   Tracking   News      AI Analysis
           |        |         |         |         |               |
           +------------------------------------------------------+
                              |
                       Repository Layer
                              |
                           SQLite
                              |
               +-------------------------------+
               | External Provider Interfaces  |
               +-------------------------------+
               | Market Data                   |
               | News                          |
               | AI                            |
               | Notifications                 |
               +-------------------------------+
```

---

# Architecture Principles

- Layered architecture
- Separation of concerns
- Local-first persistence
- Real-time market data
- Plugin-ready design
- Simple, maintainable modules
- Manual review of AI-generated code

---

# Frontend Responsibilities

- Search UI
- Chart rendering
- Technical indicators
- Drawing tools
- Position & Target Tracking UI
- AI Insights
- News
- Alerts
- Workspace management

The frontend never communicates directly with external providers.

---

# Backend Responsibilities

- Expose REST APIs
- Call external providers
- Calculate indicators
- Manage workspaces
- Manage Position & Target Tracking
- Generate alerts
- Persist local data
- Handle provider failover

---

# Backend Modules

- Search Service
- Chart Service
- Indicator Service
- Tracking Service
- News Service
- AI Service
- Alert Service
- Workspace Service
- Drawing Service

---

# Position & Target Tracking

A dedicated Tracking Service manages three tracking states:

- UNTRACKED
- OWNED
- WATCH

Responsibilities:

- Save tracking information
- Update tracking information
- Evaluate buy targets
- Evaluate sell targets
- Evaluate stop-loss conditions
- Trigger alerts

Tracking is informational only and is not portfolio management.

---

# Data Flow

## Stock Analysis

```text
Search Stock
      |
Frontend
      |
Search API
      |
Search Service
      |
Market Provider
      |
Chart API
      |
Chart Service
      |
Indicators
      |
Frontend Chart
```

## Tracking

```text
User saves tracking
        |
Tracking API
        |
Tracking Service
        |
SQLite
        |
Alert Engine
```

---

# External Providers

Abstract provider interfaces are used for:

- Market data
- News
- AI
- Notifications

This allows providers to be replaced without changing business logic.

---

# Persistence

SQLite stores:

- Workspaces
- Notes
- Drawings
- Alerts
- Preferences
- Position Tracking
- User-approved historical snapshots

Market data is retrieved on demand.

---

# Error Handling

- Central exception handling
- Consistent REST error responses
- User-friendly UI messages

---

# Logging

Log:

- API requests
- Provider failures
- Tracking events
- Alert events
- Workspace operations

Never log secrets or API keys.

---

# Security

- No login in Version 1
- Local desktop application
- API keys stored outside source code
- Validate all API input

---

# Deployment

Development:

- React Development Server
- Spring Boot
- SQLite

Production:

- Single local desktop deployment with embedded database.

---

# Acceptance Criteria

Architecture supports:

- Search
- Charts
- Indicators
- Position & Target Tracking
- AI Insights
- News
- Alerts
- Workspaces
- Plugin-ready future expansion
