# Tekkeyah

> A full-stack charity and event management system developed with **Spring Boot**, **Hibernate ORM**, **Microsoft SQL Server**, and **React (Vite)**, demonstrating core Object-Oriented Software Design Patterns.

---

## 🏛️ System Architecture

The project is structured as a decoupled monorepo with distinct **Server** (REST API) and **Client** (Single Page Application) layers.

```text
Tekkeyah-SDP-Implementation/
├── Client/                         # React Frontend (Vite)
│   ├── public/                     # Static assets
│   ├── src/                        # React source code (components, pages, services)
│   ├── index.html                  # App entrypoint
│   ├── package.json                # Frontend dependencies & scripts
│   └── vite.config.js              # Vite configuration
│
├── Server/                         # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/tekkeyah/
│   │   │   │   ├── config/         # App configuration (e.g., CorsConfig)
│   │   │   │   ├── controller/     # REST Controllers exposing JSON APIs
│   │   │   │   ├── service/        # Business logic & pattern orchestration
│   │   │   │   ├── repository/     # Spring Data JPA repositories (extending JpaRepository)
│   │   │   │   ├── model/
│   │   │   │   │   └── entities/   # JPA Entities mapped to SQL Server tables
│   │   │   │   ├── dto/            # Data Transfer Objects (payload contracts)
│   │   │   │   ├── pattern/        # Design Pattern Implementations
│   │   │   │   │   ├── observer/   # Subject & Observer interfaces and handlers
│   │   │   │   │   ├── strategy/   # NotificationStrategy & PaymentStrategy
│   │   │   │   │   ├── decorator/  # VolunteerRole & dynamic decorators
│   │   │   │   │   ├── factory/    # EventCreator factory methods
│   │   │   │   │   └── singleton/  # DatabaseConnection singleton
│   │   │   │   ├── exception/      # Global exception handling & custom errors
│   │   │   │   └── TekkeyahApplication.java # Spring Boot main entry
│   │   │   └── resources/
│   │   │       └── application.properties   # DB credentials & Hibernate configs
│   │   └── test/                   # Unit and integration tests
│   ├── mvnw / mvnw.cmd             # Maven Wrapper scripts
│   └── pom.xml                     # Maven project configuration & dependencies
│
└── README.md                       # Project setup & documentation
```

---

## 🧩 Design Patterns Overview

Based on the system class diagram, the core design patterns implemented are:

| Pattern | Role in System | Key Classes / Interfaces |
| :--- | :--- | :--- |
| **Observer** | Notifies users and entities upon state changes (e.g. event status updates, donation receipts, payment status). | `Subject`, `Observer`, `Event` (`Fundraiser`, `Outreach`), `Donation`, `Payment`, `User` (`Volunteer`, `Donor`, `Beneficiary`, `Admin`). |
| **Strategy (Notification)** | Allows users to dynamically select their preferred notification channel. | `NotificationStrategy`, `EmailNotification`, `SMSNotification`, `WhatsAppNotification`. |
| **Strategy (Payment)** | Supports interchangeable payment methods for ticket purchases and donations. | `PaymentStrategy`, `CardPayment`, `CashPayment`. |
| **Decorator** | Dynamically attaches additional responsibilities and permissions to volunteer roles. | `VolunteerRole` (Component), `BasicVolunteerRole` (Concrete Component), `VolunteerRoleDecorator` (Decorator), `DriverRole`, `CookRole` (Concrete Decorators). |
| **Factory Method** | Encapsulates the instantiation and configuration of specialized event types. | `EventCreator` (Creator), `FundraiserCreator`, `OutreachCreator` (Concrete Creators), `Event` (Product). |
| **Singleton** | Ensures a single shared instance for direct database operations. | `DatabaseConnection`. |

---

## ⚙️ Prerequisites

Ensure the following tools are installed on your machine:
* **Java Development Kit (JDK)**: JDK 17 or higher (JDK 17 LTS / 21 LTS / 24).
* **Node.js**: v18.x or higher (along with `npm`).
* **Database**: **Microsoft SQL Server** (or local instance) + SQL Server Management Studio (SSMS).

---

## 🚀 Getting Started & Execution

### 1. Database Setup (Microsoft SQL Server)

1. Open **SQL Server Management Studio (SSMS)** and connect with Windows Authentication.
2. Create the database:
   ```sql
   CREATE DATABASE tekkeyah_db;
   ```
3. Ensure **Mixed Mode Authentication** is enabled:
   * Right-click Server in Object Explorer $\rightarrow$ **Properties** $\rightarrow$ **Security** $\rightarrow$ Select **SQL Server and Windows Authentication mode** $\rightarrow$ Click **OK**.
4. Enable and set a password for the `sa` login:
   * Expand **Security** $\rightarrow$ **Logins** $\rightarrow$ Right-click `sa` $\rightarrow$ **Properties**.
   * Set password (e.g. `1234`).
   * In **Status**, set *Login* to **Enabled** $\rightarrow$ Click **OK**.
5. **Restart SQL Server**:
   * Right-click the server instance in SSMS $\rightarrow$ Click **Restart** (or run `Restart-Service MSSQLSERVER` in an Admin terminal).

---

### 2. Backend Setup (Spring Boot)

1. Check your database settings in `Server/src/main/resources/application.properties`:
   ```properties
   server.port=8080

   # Database Connection
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=tekkeyah_db;encrypt=true;trustServerCertificate=true
   spring.datasource.username=sa
   spring.datasource.password=1234
   spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

   # Hibernate ORM (Auto Table Generation)
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```
2. Navigate to the `Server` directory and run the application:
   * **Windows (PowerShell / CMD)**:
     ```powershell
     cd Server
     .\mvnw.cmd spring-boot:run
     ```
   * **macOS / Linux**:
     ```bash
     cd Server
     ./mvnw spring-boot:run
     ```
3. The server starts at **`http://localhost:8080`**. Hibernate will automatically create or update all SQL tables on startup.

---

### 3. Frontend Setup (React + Vite)

1. Navigate to the `Client` directory:
   ```bash
   cd Client
   ```
2. Install frontend dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
4. Open your browser and navigate to **`http://localhost:5173`**.
