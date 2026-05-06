# NGOConnect
A JavaFX desktop app to discover, search, and connect with NGOs &amp; Self-Help Groups across India — built with layered MVC architecture, Stream API, and file-based persistence.
NGOConnect bridges the gap between volunteers, donors, and social organisations — all through a clean, layered Java application.

---

## Features

- **Search & Discovery** — keyword search across name, description, location, area of work, and categories
- **Category Filtering** — 8 cause categories: Old Age, Women Empowerment, Child Welfare, Animal Welfare, Environment, Specially-Abled, Education, Health
- **Detailed Org Profiles** — contact info, founding year, tenure, recent activities, events, and volunteer count
- **Reviews & Ratings** — 5-star rating system with comments, averaged and displayed on every card
- **Connect & Collaborate** — express interest, volunteer, or directly contact an organisation
- **Admin Panel** — register new NGOs/SHGs, verify organisations, manage events
- **SDG Alignment** — organisations tagged to relevant UN Sustainable Development Goals (SDG 3, 4, 6, 8, 10, 11, 12, 13, 15, 17)
- **File Persistence** — data saved using Java Serialisation (`organisations.dat`, `users.dat`)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI Framework | JavaFX 11+ (openjfx) |
| Build Tool | Apache Maven 3 |
| Persistence | Java Serialisation (ObjectOutputStream / InputStream) |
| Module System | JPMS (`module-info.java`) |

---

## Architecture

The project follows a strict 4-layer architecture:

```
UI Layer          →  HomeView, SearchView, BrowseView, AccountView, AdminView, OrgProfileView
Service Layer     →  OrganisationService, UserService, DataInitializerService
Repository Layer  →  OrganisationRepository, UserRepository, FileRepository
Model Layer       →  Organisation, User, Category, OrgType, ContactInfo, Event, Review
```

---

## Project Structure

```
src/
└── main/
    └── java/
        └── com/ngoconnect/
            ├── MainApp.java
            ├── model/
            │   ├── Organisation.java
            │   ├── User.java
            │   ├── Category.java
            │   ├── OrgType.java
            │   ├── ContactInfo.java
            │   ├── Event.java
            │   └── Review.java
            ├── repository/
            │   ├── OrganisationRepository.java
            │   ├── UserRepository.java
            │   └── FileRepository.java
            ├── service/
            │   ├── OrganisationService.java
            │   ├── UserService.java
            │   └── DataInitializerService.java
            ├── ui/
            │   ├── HomeView.java
            │   ├── SearchView.java
            │   ├── BrowseView.java
            │   ├── AccountView.java
            │   ├── OrgProfileView.java
            │   └── AdminView.java
            ├── util/
            │   └── Validator.java
            └── exception/
                ├── NGOConnectException.java
                └── OrganisationNotFoundException.java
```

---

## Java Concepts Applied

- **JavaFX Scene Graph** — BorderPane, VBox, HBox, FlowPane, ScrollPane layout containers
- **Stream API** — `filter()`, `map()`, `sorted()`, `limit()`, `toList()` pipelines
- **Lambda expressions** — `EventHandler`, `Runnable` stored as variables and shared across triggers
- **Method references** — `Organisation::isVerified`, `Double::compare`
- **Custom exception hierarchy** — `NGOConnectException` → `OrganisationNotFoundException`
- **Java Serialisation** — `Serializable`, `serialVersionUID`, binary `.dat` file persistence
- **Collections framework** — `ArrayList`, `List`, `Optional`
- **Input validation** — regex-based email/phone checks, range validation, safe parsing

---

## Getting Started

### Prerequisites

- JDK 11 or higher
- JavaFX 11+ SDK (or use the Maven dependency via `pom.xml`)
- Maven 3.x

### Run

```bash
git clone https://github.com/your-username/NGOConnect.git
cd NGOConnect
mvn clean javafx:run
```

> On first run, `DataInitializerService` seeds 20+ real NGOs and SHGs automatically.

---
Thank youu!
