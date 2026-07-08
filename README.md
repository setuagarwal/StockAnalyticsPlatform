# Stock Analytics Platform

> A personal desktop application for technical analysis of Indian stock market securities listed on NSE and BSE.

## Project Status

**Phase:** Documentation & Architecture (Ready to Begin Implementation)

The project is being built incrementally using AI-assisted development. Every feature is documented, implemented, reviewed, approved, and then committed to the codebase.

---

# Vision

Build a fast, reliable, maintainable and extensible desktop application that provides professional-grade stock analysis without requiring paid infrastructure.

---

# Key Features

- Search stocks across NSE and BSE
- Interactive candlestick charts
- Multiple technical indicators
- Manual drawing tools
- AI-assisted technical analysis
- Company, sector and market news
- **Position & Target Tracking**
- Alerts (Desktop, Email and WhatsApp)
- Notes and saved workspaces
- Plugin-ready architecture

---

# Technology Stack

| Layer | Technology |
|------|------------|
| Frontend | React + TypeScript + Material UI |
| Backend | Java 21 + Spring Boot |
| Database | SQLite |
| Charts | TradingView Lightweight Charts |
| Build | Maven + npm |
| Version Control | Git + GitHub |

---

# Repository Structure

```text
StockAnalyticsPlatform/
│
├── README.md
├── docs/
│   └── handbook/
├── frontend/
├── backend/
├── database/
├── plugins/
├── prompts/
└── workspace/
```

---

# Documentation

The project handbook currently consists of:

```text
docs/handbook/

01-project.md
02-architecture.md
03-ui.md
04-api.md
05-database.md
06-external-providers.md
```

These documents together define the product, architecture, UI, APIs, database, and external integrations.

---

# Development Workflow

1. Update documentation
2. Generate implementation
3. Review generated code
4. Approve implementation
5. Commit to Git

**No AI-generated code is added to the project without manual review and approval.**

---

# Position & Target Tracking

The application supports three tracking modes:

- Untracked
- Owned Position
- Watch Position

Tracking is intended solely for personal monitoring and alerting. It is **not** portfolio management.

---

# Getting Started

```bash
git clone https://github.com/setuagarwal/StockAnalyticsPlatform.git
cd StockAnalyticsPlatform
```

---

# Current Version

**Version:** 1.0 (Documentation Complete – Implementation Ready)

---

This repository is the single source of truth for the Stock Analytics Platform.
