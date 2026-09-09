# Airport Ticket Reservation System

## Overview
The **Airport Ticket Reservation System** is a robust, enterprise-grade software application designed to manage the daily operations of a flight booking agency. The project is built using a modern **Distributed Microservices Architecture** for the backend and a rich **JavaFX Desktop Client** for the frontend, ensuring high scalability, maintainability, and a seamless user experience. 

The system strictly adheres to **Domain-Driven Design (DDD)** and **Hexagonal Architecture (Ports and Adapters)** principles, completely decoupling business logic from underlying technical frameworks and databases.

## Key Features
The application provides tailored functionalities based on Role-Based Access Control (RBAC):

*   **Public / Unauthenticated Users:** 
    *   Search for available flights between departure and arrival airports.
    *   View flight details, schedules, pricing, and available seats in real-time.
*   **Employees:**
    *   Access an interactive, color-coded visual map of airplane seats.
    *   Process ticket sales, update passenger details, and cancel reservations.
    *   Export individual tickets or complete flight manifests into multiple formats (PDF, CSV, JSON, XML, DOC).
*   **Managers:**
    *   Perform full CRUD operations on the flight inventory and routes.
    *   Access graphical dashboards (Bar Charts, Pie Charts) to monitor key performance metrics like revenue per flight, average seat occupancy, and top destinations.
*   **Administrators:**
    *   Manage agency personnel accounts (Employees and Managers).
    *   Automatically trigger multi-channel security notifications (Email, SMS, WhatsApp) whenever user authentication details are updated.

## Architecture & Technology Stack

### Backend (Microservices)
*   **Core:** Java 23, Spring Boot 3.4.1
*   **Routing & Security:** Spring Cloud Gateway (Single Point of Entry API Gateway)
*   **Persistence:** Spring Data JPA, Hibernate ORM, MySQL (Database-per-service pattern)
*   **API Documentation:** Swagger / OpenAPI
*   **Ecosystem:** 6 Independent Microservices (*Flights, Tickets, Users, Notifications, Statistics, Export*)

### Frontend (Desktop Client)
*   **Framework:** JavaFX with FXML for declarative UI design.
*   **Architecture:** MVVM (Model-View-ViewModel) enforcing strict separation of presentation and logic.
*   **Internationalization (i18n):** Multi-language support dynamically loaded via Java ResourceBundles.

## Design Patterns Applied
To ensure a flexible and maintainable codebase, the project integrates fundamental Gang of Four (GoF) design patterns:
1.  **Singleton:** Managed via Spring IoC container for stateless backend services and UI Language Management.
2.  **Adapter:** Bridges the Domain layer with Infrastructure (e.g., JPA Repositories and Feign HTTP Clients).
3.  **Flyweight:** Optimizes memory usage in the JavaFX client when rendering large interactive flight seat maps.
4.  **Command:** Encapsulates UI actions in the MVVM frontend, facilitating asynchronous execution and non-blocking UI.
5.  **Strategy:** Dynamically selects the appropriate document generation algorithm (PDF, CSV, etc.) at runtime in the Export microservice.
