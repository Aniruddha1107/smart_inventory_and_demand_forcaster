# 📦 Smart Inventory and Demand Forecaster

A comprehensive Warehouse Management System (WMS) built using Spring Boot, focusing on inventory tracking, sales processing, customer RFM segmentation, and AI-driven demand forecasting.

## 🌟 Key Features

- **Inventory Tracking**: A dashboard to list, add, edit, and soft-delete products. Tracks both current stock and safety stock levels.
- **Sales Logging**: Create and track sales mapped between customers and products, which dynamically decrement and adjust inventory levels in real-time.
- **Alert System**: Automatically flags out-of-stock and low-stock items based on predefined safety thresholds, notifying admins with active alerts.
- **Customer Insights (RFM)**: Segments customers based on their purchasing habits across Recency, Frequency, and Monetary Value into distinct cohorts (e.g., Champions, Loyal, At-Risk, Lost).
- **AI Demand Forecasting**: Integrates with an external Python ML service (`/forecast`) over internal HTTP to predict 30-day product demand curves, displayed neatly via interactive Chart.js graphs.
- **Premium User Interface**: Built entirely with server-side Thymeleaf templates and fragments, backed by clean CSS architecture.
- **Role-Based Auth**: Secured routes using Spring Security, limiting access based on `ADMIN` and `MANAGER` roles.

## 🛠️ Technology Stack

**Backend System**:
- Java 17
- Spring Boot 3.x (Web, Data JPA, Security)
- MySQL 8.0 (Database Storage)
- Lombok

**Frontend UI**:
- HTML5, Vanilla modern CSS (Flexbox/Grid architecture)
- Thymeleaf Template Engine
- Chart.js (Data visualization)

**AI Microservice (External Integration)**:
- Python FastAPI
- Statsmodels / Prophet (TimeSeries predictions)

## 📂 Project Structure

```
smartinventory/
├── src/main/java          # Spring Boot Application Backend
│   ├── controller         # MVC HTTP Request Mappers (Dashboard, Products, Sales, Forecast)
│   ├── service            # Core Business Logic (RFM, Alerts, Users)
│   ├── repository         # JpaRepository Data Access Layer
│   ├── entity             # Database ORMs
│   ├── dto                # Data Transfer Objects for Forms
│   └── security           # Access Control and Auth Configurations 
├── src/main/resources     # Static Assets and UI
│   ├── templates/         # Thymeleaf views (auth, sales, customers, products)
│   ├── static/css/        # Stylings
│   └── application.properties # App configurations
└── pom.xml                # Maven Dependencies
```

## 🚀 Getting Started

### Prerequisites
- JDK 17
- Maven 3.6+
- MySQL 8.0 installed locally
- Python 3.9+ (For the external forecast service)

### Backend Setup
1. Open MySQL and ensure the database specified in `application.properties` is created.
2. The project's entities will auto-generate the schema using Hibernate (`spring.jpa.hibernate.ddl-auto=update`).
3. Run the Spring Boot Application via your IDE or terminal:
   ```bash
   mvn spring-boot:run
   ```
4. Access the web interface at `http://localhost:8083/`.

### Prediction Service Setup
*(Ensure you have the API started alongside the backend to view forecasts)*
1. Navigate to the `forecast-service` directory.
2. Install pip requirements: `pip install -r requirements.txt`.
3. Start the FastAPI server using uvicorn. The backend Spring Boot configuration defaults to pinging port `8000` for predictions.

## 🔒 Default Roles
* **ADMIN**: Has unrestricted access, can register new system users (`/auth/register`), and view all analytics.
* **MANAGER**: Operations access, can view dashboards, manage product lines, and log sales.

---
*Built as an extensive tutorial project for data-driven warehouse management.*
