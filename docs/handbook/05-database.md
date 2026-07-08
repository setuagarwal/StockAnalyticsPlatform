# Stock Analytics Platform - Database Specification

## Purpose

Defines the Version 1 SQLite database for the Stock Analytics Platform.

The database stores only user-specific information and user-approved snapshots.
Market data is fetched on demand.

---

# Database

- SQLite
- Spring Data JPA
- Hibernate

---

# Core Tables

## instruments

Stores instruments referenced by the user.

Key fields:

- exchange
- symbol
- company_name
- instrument_type
- sector
- industry

---

## workspaces

Stores saved analysis workspaces.

Includes:

- selected instrument
- timeframe
- layout
- AI snapshot

---

## workspace_indicators

Stores selected indicators and configuration.

---

## drawings

Stores chart drawings.

---

## notes

Stores research notes linked to an instrument and/or workspace.

---

## alerts

Stores alert definitions.

Supported alerts:

- Price
- Buy Target
- Sell Target
- Stop-loss
- Indicator
- Pattern
- Volume

---

## position_tracking

Stores tracking information for analysed stocks.

### tracking_type

- UNTRACKED
- OWNED
- WATCH

### Fields

- instrument_id
- tracking_type
- average_buy_price
- quantity
- target_sell_price
- stop_loss_price
- target_buy_price
- target_buy_range_low
- target_buy_range_high
- notes
- alerts_enabled
- created_at
- updated_at

### Behaviour

#### OWNED

Stores:

- Average buying price
- Quantity
- Optional target sell price
- Optional stop-loss
- Optional notes

#### WATCH

Stores:

- Target buying price
- Target buying range
- Optional stop-loss
- Optional notes

#### UNTRACKED

No tracking information is stored.

---

## user_preferences

Stores application preferences.

Examples:

- theme
- default timeframe
- last workspace
- default layout

---

## historical_snapshots

Stores user-approved historical market data snapshots.

---

## provider_cache

Optional short-lived cache for provider responses.

---

# Relationships

```text
Instrument
 |
 +-- Workspace
 |
 +-- Position Tracking
 |
 +-- Drawings
 |
 +-- Notes
 |
 +-- Alerts
 |
 +-- Historical Snapshot

Workspace
 |
 +-- Indicators
 +-- Drawings
 +-- Notes
```

---

# Storage Rules

Persist:

- Workspaces
- Tracking
- Notes
- Drawings
- Alerts
- Preferences
- User-approved snapshots

Do not persist by default:

- Entire market history
- Live provider responses
- API secrets

---

# Acceptance Criteria

The database supports:

- Workspace persistence
- Indicator persistence
- Position & Target Tracking
- Alerts
- Notes
- Drawings
- Preferences
- Historical snapshots
