# Tekkeyah (تكية)

> A full-stack charity, mutual aid, and event management platform developed with **React (Vite)**, **Spring Boot**, **Hibernate ORM**, and **Microsoft SQL Server**, demonstrating foundational Object-Oriented Software Design Patterns (SDP).

---

## 🏛️ System Architecture & Directory Roles

The repository is organized as a decoupled monorepo separating the frontend Single Page Application (`Client`) from the backend REST API (`Server`), with centralized project documentation (`docs`).

```text
Tekkeyah-SDP-Implementation/
├── Client/                         # Frontend React SPA (Vite)
│   ├── public/                     # Static web assets & icons
│   ├── src/
│   │   ├── assets/                 # SVGs, imagery, and static graphics
│   │   ├── components/             # Decoupled UI component library
│   │   │   ├── common/             # Reusable design primitives (Buttons, Badges, Cards, Modals, Empty States)
│   │   │   ├── layout/             # Global structural framing (Navbar, Footer, App chrome)
│   │   │   ├── landing/            # Landing page domain sections (Hero, Action Cards, Transparency Ledger, Charter)
│   │   │   ├── auth/               # Authentication widgets (AuthCard, AuthHeroPanel, Role pickers)
│   │   │   └── giving/             # Donation & Giving Hub widgets (Widget, History Table, Receipt Modal, Header)
│   │   ├── data/                   # Decoupled fallback contracts & presets (prevents JSX clutter)
│   │   ├── pages/                  # Route-level container views (Landing, Auth, Giving Hub, NotFound)
│   │   ├── services/               # API clients, HTTP wrappers, and user session storage
│   │   ├── styles/                 # Global design tokens, palette variables, and CSS utilities
│   │   ├── App.jsx                 # Client routing tree, global state & session hydration
│   │   ├── index.css               # Global baseline stylesheet & font resets
│   │   └── main.jsx                # React root bootstrap entrypoint
│   ├── index.html                  # HTML5 entrypoint & Google Fonts declarations
│   ├── package.json                # Frontend dependencies, scripts & dev dependencies
│   └── vite.config.js              # Vite bundling & development server configuration
│
├── Server/                         # Backend REST API (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/tekkeyah/
│   │   │   │   ├── config/         # System configuration (CORS, security headers, global beans)
│   │   │   │   ├── controller/     # REST Controllers exposing JSON endpoints to frontend
│   │   │   │   ├── service/        # Business logic & pattern orchestration
│   │   │   │   ├── repository/     # Spring Data JPA data access interfaces (extending JpaRepository)
│   │   │   │   ├── model/
│   │   │   │   │   ├── entity/     # Hibernate JPA Entities mapped to SQL Server tables
│   │   │   │   │   └── DTO/        # Data Transfer Objects for decoupled API contracts
│   │   │   │   ├── pattern/        # Core Object-Oriented Design Patterns
│   │   │   │   │   ├── factory/    # Factory Method: Encapsulated Event & Contribution creation
│   │   │   │   │   ├── strategy/   # Strategy Pattern: Interchangeable Payment & Notification behaviors
│   │   │   │   │   ├── observer/   # Observer Pattern: Subject-Observer event broadcast for updates & receipts
│   │   │   │   │   ├── decorator/  # Decorator Pattern: Dynamic runtime addition of Volunteer capabilities
│   │   │   │   │   └── singleton/  # Singleton Pattern: Shared, thread-safe database connection instance
│   │   │   │   ├── exception/      # Global exception handler (@ControllerAdvice) and custom runtime errors
│   │   │   │   ├── util/           # Common helper utilities, date parsers & formatters
│   │   │   │   └── TekkeyahApplication.java # Spring Boot application runner
│   │   │   └── resources/
│   │   │       └── application.properties   # Database connection pool & Hibernate ORM configs
│   │   └── test/                   # Unit & integration test suites
│   ├── mvnw / mvnw.cmd             # Maven Wrapper scripts for reproducible builds
│   └── pom.xml                     # Maven project configuration & dependency manifest
│
├── docs/                           # Architecture specifications, diagrams & UML assets
└── README.md                       # Comprehensive setup guide & architectural documentation
```

---

## 📂 Detailed Directory Responsibilities

### 💻 Client (`Client/src/`)

| Directory | Important Role & Responsibility | Key Contents |
| :--- | :--- | :--- |
| **`components/common/`** | **Reusable Primitives:** Stateless, highly reusable UI components styled to enforce design token consistency across all pages. | `Button`, `Card`, `Badge`, `Modal`, `EmptyState` |
| **`components/layout/`** | **Global Shell:** Framing components that wrap views, handling global brand navigation, active route highlights, user session pills, and footer links. | `Navbar`, `Footer` |
| **`components/landing/`** | **Landing Page Domain:** Modular presentation blocks for the main public portal, highlighting the mission, direct calls-to-action, public transparency ledger, and community charter. | `LandingHero`, `ActionCards`, `PublicLedgerBanner`, `CampaignsSection`, `CommunityCharter`, `CtaBanner` |
| **`components/auth/`** | **Authentication Domain:** Split-screen authentication widgets supporting Sign In, Sign Up, role selection (Donor, Volunteer, Beneficiary), and session persistence. | `AuthCard`, `AuthHeroPanel` |
| **`components/giving/`** | **Donation & Audit Portal:** Multi-step donation widget (Factory & Strategy patterns), audit log table, receipt generator modal, and navigation tabs. | `GivingHeader`, `ContributionWidget`, `ReceiptsHistoryTable`, `ReceiptModal` |
| **`pages/`** | **Route Containers:** Top-level view controllers mapped to browser URL routes via React Router. Coordinate layout, state, and child domain components. | `LandingPage`, `AuthPage`, `GivingHubPage`, `NotFoundPage` |
| **`services/`** | **Decoupled API Layer:** Encapsulates network communication and session caching. Keeps React components completely free of direct `fetch` / `axios` calls. | `api.js` (HTTP client), `authService.js` (Session management) |
| **`data/`** | **Decoupled Preset Contracts:** Stores mock records and schema presets in standalone files so page components remain clutter-free per design requirements. | `donationPresets.js` |
| **`styles/`** | **Design Tokens:** Centralized CSS custom properties establishing the grounded natural palette (earthy sage, deep forest green, warm white), typography, and shadows. | `variables.css` |

---

### ☕ Server (`Server/src/main/java/com/tekkeyah/`)

| Directory | Important Role & Responsibility | Key Contents |
| :--- | :--- | :--- |
| **`config/`** | **Infrastructure Configuration:** Defines Spring `@Configuration` beans, including Cross-Origin Resource Sharing (`CorsConfig`) to allow seamless Client-Server communication. | `CorsConfig.java` |
| **`controller/`** | **REST Presentation Layer:** Exposes HTTP endpoints (`@RestController`), parses JSON request payloads, delegates processing to services, and returns standardized HTTP responses. | `AuthController`, `DonationController`, `EventController` |
| **`service/`** | **Business Logic Layer:** Encapsulates domain business rules, coordinates transaction boundaries (`@Transactional`), and orchestrates design pattern behaviors. | `UserService`, `DonationService`, `EventService` |
| **`repository/`** | **Data Persistence Layer:** Spring Data JPA interfaces that execute type-safe SQL queries and handle CRUD interactions against Microsoft SQL Server. | `UserRepository`, `DonationRepository`, `EventRepository` |
| **`model/entity/`** | **JPA Database Entities:** Java classes mapped directly to SQL database tables using Hibernate annotations (`@Entity`, `@Table`, `@Id`, `@OneToMany`). | `User`, `Volunteer`, `Donor`, `Beneficiary`, `Event`, `Donation`, `Payment` |
| **`model/DTO/`** | **Data Transfer Objects:** Decoupled contract records/classes used for API requests and responses, preventing direct exposure of internal database entities. | `LoginRequestDTO`, `RegisterRequestDTO`, `DonationDTO` |
| **`pattern/`** | **Design Pattern Implementations:** Explicit separation of classic GoF Object-Oriented patterns to meet Software Design Patterns course requirements. | Sub-packages for each design pattern |
| **`pattern/factory/`** | **Factory Method Pattern:** Encapsulates object creation logic for specialized event types and contribution types without coupling clients to concrete classes. | `EventCreator`, `FundraiserCreator`, `OutreachCreator`, `DonationFactory` |
| **`pattern/strategy/`** | **Strategy Pattern:** Defines families of interchangeable algorithms for payments and notifications, allowing runtime switching without modifying context classes. | `PaymentStrategy` (`CardPayment`, `CashPayment`), `NotificationStrategy` (`EmailNotification`, `SMSNotification`, `WhatsAppNotification`) |
| **`pattern/observer/`** | **Observer Pattern:** Implements loosely-coupled one-to-many dependency so that when an event or donation changes state, all registered observers are notified automatically. | `Subject`, `Observer`, `DonationSubject`, `ReceiptObserver`, `NotificationService` |
| **`pattern/decorator/`** | **Decorator Pattern:** Dynamically extends volunteer capabilities (e.g., adding driver or kitchen credentials) at runtime without class explosion or subclassing. | `VolunteerRole`, `BasicVolunteerRole`, `VolunteerRoleDecorator`, `DriverRole`, `CookRole` |
| **`pattern/singleton/`** | **Singleton Pattern:** Guarantees a single, globally accessible instance for direct database connection management and resource pooling. | `DatabaseConnection` |
| **`exception/`** | **Error Handling Layer:** Centralizes application exceptions and HTTP error mappings with `@ControllerAdvice` for consistent error JSON responses. | `GlobalExceptionHandler`, `ResourceNotFoundException`, `UnauthorizedException` |
| **`util/`** | **Helper Utilities:** Static utility classes for cryptographic hashing, token formatting, date conversions, and text sanitization. | `DateUtils`, `ValidationUtils` |

---

## 🧩 Design Patterns Summary

| Pattern | Architectural Role in Tekkeyah | Concrete Classes & Interfaces |
| :--- | :--- | :--- |
| **Factory Method** | Encapsulates the instantiation of different charitable events and donation products. | `EventCreator` (Creator) $\rightarrow$ `FundraiserCreator`, `OutreachCreator`<br>`Event` (Product) $\rightarrow$ `FundraiserEvent`, `OutreachEvent` |
| **Strategy (Payment)** | Enables dynamic runtime selection of payment processing algorithms. | `PaymentStrategy` (Strategy) $\rightarrow$ `CardPayment`, `CashPayment`<br>`PaymentService` (Context) |
| **Strategy (Notification)** | Allows donors and volunteers to configure preferred real-time notification channels. | `NotificationStrategy` (Strategy) $\rightarrow$ `EmailNotification`, `SMSNotification`, `WhatsAppNotification` |
| **Observer** | Dispatches real-time state changes (e.g. donation verification, event milestones) to registered user observers. | `Subject` $\rightarrow$ `Donation`, `Event`<br>`Observer` $\rightarrow$ `User` (`Donor`, `Volunteer`, `Beneficiary`) |
| **Decorator** | Dynamically attaches special skills, privileges, and responsibilities to volunteers at runtime. | `VolunteerRole` (Component) $\rightarrow$ `BasicVolunteerRole`<br>`VolunteerRoleDecorator` $\rightarrow$ `DriverRole`, `CookRole` |
| **Singleton** | Ensures a single shared connection instance for low-level database operations. | `DatabaseConnection` |

---

## ⚙️ Prerequisites

* **Java Development Kit (JDK)**: JDK 17 LTS or higher (e.g., JDK 21 LTS).
* **Node.js**: v18.x or higher with `npm`.
* **Database**: **Microsoft SQL Server** (2019/2022 or LocalDB) + SQL Server Management Studio (SSMS).

---

## 🚀 Getting Started & Execution

### 1. Database Setup (Microsoft SQL Server)

1. Open **SQL Server Management Studio (SSMS)** and connect with Windows Authentication.
2. Create the project database:
   ```sql
   CREATE DATABASE tekkeyah_db;
   GO
   ```
3. Ensure **Mixed Mode Authentication** is active:
   * Right-click the server instance in Object Explorer $\rightarrow$ **Properties** $\rightarrow$ **Security** $\rightarrow$ Select **SQL Server and Windows Authentication mode** $\rightarrow$ Click **OK**.
4. Enable and set a password for the `sa` login:
   * Expand **Security** $\rightarrow$ **Logins** $\rightarrow$ Right-click `sa` $\rightarrow$ **Properties**.
   * Set a password (e.g., `1234`).
   * On the **Status** page, ensure *Login* is set to **Enabled** $\rightarrow$ Click **OK**.
5. Restart SQL Server to apply authentication changes:
   * In SSMS, right-click the server instance $\rightarrow$ Click **Restart** (or run `Restart-Service MSSQLSERVER` in an Administrator PowerShell).

---

### 2. Backend Setup (Spring Boot)

1. Verify database credentials in `Server/src/main/resources/application.properties`:
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
2. Navigate to the `Server` directory and start the application:
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
3. The server starts at **`http://localhost:8080`**. Hibernate automatically creates or updates tables on startup.

---

### 3. Frontend Setup (React + Vite)

1. Navigate to the `Client` directory:
   ```bash
   cd Client
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm run dev
   ```
4. Open your browser and navigate to **`http://localhost:5173`**.
   * **Landing Page**: `http://localhost:5173/`
   * **Authentication Portal**: `http://localhost:5173/auth`
   * **Giving Hub**: `http://localhost:5173/giving-hub`

---

## 🧪 Code Quality & Verification

```bash
# In Client/ directory
npm run lint      # Check ESLint compliance (0 errors, 0 warnings)
npm run build     # Verify production bundle build
```
