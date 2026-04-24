# 📦 Smart Inventory

> An AI-enhanced Inventory Management System built with Spring Boot, Thymeleaf, MySQL, and a Python-based demand forecasting microservice.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Modules](#modules)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setup--installation)
- [Starting the Application](#starting-the-application)
- [User Manual](#user-manual)
- [Default Credentials](#default-credentials)
- [Tech Stack](#tech-stack)

---

## Overview

Smart Inventory is a full-stack web application designed for small-to-medium businesses to manage their inventory lifecycle end-to-end. It combines traditional inventory management with AI-driven demand forecasting and customer analytics.

**Key capabilities:**
- Real-time stock tracking with automatic low-stock and out-of-stock alerts
- AI demand forecasting using Facebook Prophet (with moving average fallback)
- EOQ (Economic Order Quantity) reorder intelligence on every product
- RFM customer segmentation (Recency, Frequency, Monetary)
- Full purchase order / restock lifecycle
- Business reports with CSV export
- Complete inventory audit trail
- Role-based access control (ADMIN / USER)

---

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                   Browser (User)                    │
└────────────────────────┬────────────────────────────┘
                         │ HTTP
┌────────────────────────▼────────────────────────────┐
│         Spring Boot Application  (Port 8083)        │
│         Thymeleaf MVC · Spring Security             │
└──────────────┬──────────────────────────┬───────────┘
               │ JPA / Hibernate          │ REST (HTTP)
┌──────────────▼──────────┐  ┌───────────▼────────────┐
│   MySQL Database        │  │  FastAPI Forecast       │
│   smart_inventory       │  │  Service  (Port 8000)   │
│   Port 3306             │  │  Python · Prophet       │
└─────────────────────────┘  └────────────────────────┘
```

The system is a **monolithic Spring Boot MVC application** (server-side rendered with Thymeleaf) with a **separate Python microservice** that handles AI demand forecasting. The two services communicate over HTTP.

---

## Modules

| # | Module | Description |
|---|---|---|
| 1 | **User Auth** | Registration, login, role-based access (ADMIN / USER) |
| 2 | **Product Management** | Full CRUD with soft-delete, category filtering, SKU tracking |
| 3 | **Sales Management** | Record sales, auto-deduct stock, link to customers |
| 4 | **Inventory Audit Log** | Immutable log of every stock change (sale, restock, adjustment) |
| 5 | **Alerts** | Auto-created LOW_STOCK / OUT_OF_STOCK alerts; auto-resolved on restock |
| 6 | **AI Demand Forecasting** | 30-day Prophet-based forecast per product; falls back to moving average |
| 7 | **EOQ / Safety Stock** | Wilson's EOQ formula + safety stock calculation shown on every product |
| 8 | **RFM Customer Segmentation** | Classifies customers as Champions / Loyal / At-Risk / Lost |
| 9 | **Dashboard** | Summary KPIs + recent activity feed |
| 10 | **Admin Panel** | User management (add users, assign roles) |
| 11 | **Restock / Purchase Orders** | Create POs, receive stock (auto-updates inventory + audit log) |
| 12 | **Reports & Analytics** | Sales report (date-range), Inventory Valuation report, CSV export |

---

## Prerequisites

Make sure the following are installed on your machine:

| Tool | Version | Notes |
|---|---|---|
| Java JDK | 21+ | Required for Spring Boot 3.x |
| Maven | 3.9+ | Bundled via `mvnw` — no separate install needed |
| MySQL | 8.0+ | Must be running locally on port 3306 |
| Python | 3.9+ | Required for the forecast microservice |
| pip | Latest | For installing Python packages |

---

## Setup & Installation

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd "Smart Inventory"
```

### 2. Create the MySQL Database

Open MySQL Workbench or the MySQL CLI and run:

```sql
CREATE DATABASE smart_inventory;
```

> The schema is auto-created by Hibernate (`spring.jpa.hibernate.ddl-auto=update`) on first startup.

### 3. Configure Database Credentials

Set environment variables if your local MySQL credentials differ:

```properties
DB_URL=jdbc:mysql://localhost:3306/smart_inventory
DB_USERNAME=root
DB_PASSWORD=your_password
```

### 4. Set Up the Python Forecast Service

```bash
cd forecast-service
python -m venv venv

# Windows
venv\Scripts\activate

# macOS / Linux
source venv/bin/activate

pip install -r requirements.txt
```

`requirements.txt` includes: `fastapi`, `uvicorn`, `pandas`, `numpy`, `prophet`

> ⚠️ Prophet requires a C++ compiler. On Windows, install [Microsoft C++ Build Tools](https://visualstudio.microsoft.com/visual-cpp-build-tools/) first if the install fails.

### 5. (Optional) Load Seed Data

To populate the database with sample products, customers, and sales:

```sql
-- Run from smartinventory/seed_data.sql
source path/to/smartinventory/seed_data.sql;
```

---

## Starting the Application

You need to start **two services**: the Python forecast service and the Spring Boot app.

### Start the Forecast Service (Terminal 1)

```bash
cd forecast-service
venv\Scripts\activate       # Windows
# source venv/bin/activate  # macOS / Linux

uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

Verify it's running: http://localhost:8000

### Start the Spring Boot App (Terminal 2)

```bash
cd smartinventory
.\mvnw.cmd spring-boot:run      # Windows
# ./mvnw spring-boot:run        # macOS / Linux
```

The app will be available at: **http://localhost:8083**

> 💡 The forecast service **must be started before** generating forecasts, but the main app works independently for all other features.

---

## User Manual

### Logging In

Navigate to **http://localhost:8083/auth/login**

Use your registered credentials. First-time setup — register at `/auth/register` or have an ADMIN create your account.

---

### Dashboard

The landing page after login. Shows:
- **Total Products** — count of active SKUs
- **Low Stock Items** — products below their safety stock threshold
- **Active Alerts** — unresolved stock alerts
- **Total Sales** — total number of recorded sales
- **Recent Inventory Activity** — last 5 stock changes (click "View All" for the full audit log)
- **Sales Trend** — revenue chart

---

### Products

**Path:** `/products`

| Action | Who | How |
|---|---|---|
| View all products | Everyone | Navigate to Products in navbar |
| View product detail + forecast | Everyone | Click **View** on any product row |
| Add a product | ADMIN | Click **+ Add Product** |
| Edit a product | ADMIN | Click **Edit** on any product row |
| Soft-delete a product | ADMIN | Click **Delete** (product is hidden, not erased) |

**Product Detail Page** shows:
- Current stock vs. safety stock threshold
- **EOQ Analysis card**: Recommended Order Qty, Suggested Safety Stock, Reorder Point, Annual Demand, Avg Daily Demand (calculated from last 365 days)
- **AI Demand Forecast**: 30-day chart — click **Generate AI Forecast** to run it (requires Python service running)

---

### Sales

**Path:** `/sales`

- **List view** — all recorded sales transactions
- **Record a Sale** (`/sales/record`) — select a product, enter quantity sold, optionally link to a customer
  - Stock is automatically deducted
  - An inventory log entry is created
  - Alerts are auto-created if stock drops below the safety stock threshold

---

### Restock / Purchase Orders

**Path:** `/restock`

1. Click **+ New Purchase Order**
2. Enter the **Supplier Name**
3. Add one or more items (Product + Quantity + Unit Cost per item)
4. Click **Add Item** to add more rows dynamically
5. Submit — the PO is created with status **PENDING**

**Receiving a PO:**
- Click **✓ Receive** on a PENDING order
- Stock quantities are automatically increased for each item
- An inventory audit log entry (`RESTOCK`) is created per item
- Any active LOW_STOCK / OUT_OF_STOCK alerts for those products are auto-resolved

**Cancelling a PO:** ADMIN only — click **✕ Cancel** on a PENDING order.

---

### Alerts

**Path:** `/alerts`

Displays all **ACTIVE** alerts (LOW_STOCK or OUT_OF_STOCK). Alerts are:
- **Created automatically** when a sale is recorded and stock falls below the safety stock threshold
- **Auto-resolved** when a purchase order is received and stock is replenished

---

### Customers & RFM Segmentation

**Path:** `/customers`

Displays all customers with their RFM scores and segment labels.

| Segment | Meaning |
|---|---|
| 🟢 Champions | High recency, frequency, and monetary value |
| 🔵 Loyal | Regularly purchasing, moderate spend |
| 🟡 At-Risk | Haven't purchased recently despite past activity |
| 🔴 Lost | Low across all three dimensions |

Click **Recalculate RFM Dimensions** to re-run the segmentation algorithm across all customers using their full purchase history.

---

### Inventory Audit Log

**Path:** `/inventory-log`

Full, immutable history of every stock change. Each entry shows:
- **Timestamp** of the change
- **Product** (linked to product detail)
- **Change Type**: `SALE` (red), `RESTOCK` (green), `ADJUSTMENT` (yellow)
- **Qty Change**: positive (green) for restocks, negative (red) for sales
- **Performed By**: the logged-in user who triggered the change

---

### Reports & Analytics *(ADMIN only)*

**Path:** `/reports`

#### Sales Report (`/reports/sales`)
- Filter by **From / To date range**
- Shows: Total Revenue, Units Sold, Transactions, Avg Order Value
- Bar chart of top 10 products by revenue
- Full product breakdown table sorted by revenue
- **Export to CSV** button

#### Inventory Valuation (`/reports/inventory`)
- Lists every active SKU with: Unit Price × Quantity = Total Value
- Shows total portfolio value in the table footer
- **Export to CSV** button

---

### Admin — Add User *(ADMIN only)*

**Path:** `/admin/users/add`

Create a new user account. Assign either:
- `USER` — can view, record sales, create/receive purchase orders
- `ADMIN` — full access including delete, cancel PO, and reports

---

## Default Credentials

After initial setup, register your first user at `/auth/register` and assign them the `ADMIN` role.

> No default application credentials are committed. Configure local database secrets through environment variables such as `DB_USERNAME` and `DB_PASSWORD`.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Security | Spring Security (Form Login, BCrypt) |
| View Engine | Thymeleaf + Spring Security extras |
| Charts | Chart.js (CDN) |
| Build | Maven (mvnw wrapper included) |
| Forecasting | Python 3 · FastAPI · Prophet · Pandas · NumPy |
| Utilities | Lombok |

---

## Project Structure

```
Smart Inventory/
├── smartinventory/                 ← Spring Boot application
│   ├── src/main/java/com/myproject/smartinventory/
│   │   ├── config/                 ← AppConfig (RestTemplate)
│   │   ├── controller/             ← MVC Controllers
│   │   ├── dto/                    ← Data Transfer Objects
│   │   ├── entity/                 ← JPA Entities
│   │   ├── repository/             ← Spring Data JPA Repositories
│   │   ├── security/               ← Spring Security config
│   │   └── service/                ← Business logic
│   ├── src/main/resources/
│   │   ├── static/css/style.css    ← Global stylesheet
│   │   ├── templates/              ← Thymeleaf HTML templates
│   │   └── application.properties  ← App configuration
│   └── pom.xml
└── forecast-service/               ← Python FastAPI microservice
    ├── main.py                     ← Forecast endpoint
    └── requirements.txt
```
