# CRM Backend - API Service

This is the backend service for the CRM application, built with Spring Boot. It handles data persistence, business logic, and provides a RESTful API for the frontend.

## Tech Stack
* **Java 21**: The latest LTS version for modern features.
* **Spring Boot 3.x**: Core framework.
* **Spring Data JPA**: For Object-Relational Mapping (ORM).
* **PostgreSQL**: Production-grade relational database.
* **Maven**: Dependency management.

## Getting Started

### 1. Database Setup
Ensure you have a PostgreSQL instance running. Create a database named `crm_db` (or as specified in your configuration).

Update the credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crm_db
spring.datasource.username=your_username
spring.datasource.password=your_password

``````

# Running The Application

From this directory, run the following command:

```
./mvnw spring-boot:run
```

# API Documentation

## Company
<li>GET <code>/api/companies</code> - Retrieve a list of all companies.</li>

<li>GET <code>/api/companies/{id}</code>  - Retrieve details of a specific company.</li>

<li>POST <code>/api/companies</code>  - Create a new company record.</li>

<li>PUT <code>/api/companies/{id}</code>  - Update an existing company.</li>

<li>DELETE <code>/api/companies/{id}</code>  - Remove a company record.</li>

## Customer
<li>GET <code>/api/customers</code>  - Retrieve a list of all customers.</li>

<li>GET <code>/api/customers/{id}</code>  - Retrieve details of a specific customer.</li>

<li>POST <code>/api/customers</code>  - Create a new customer record.</li>

<li>PUT <code>/api/customers/{id}</code>  - Update an existing customer.</li>

<li>DELETE <code>/api/customers/{id}</code>  - Remove a customer record.</li>

## CustomerNote
<li>GET <code>/api/customerNotes</code>  - Retrieve a list of all customerNotes.</li>

<li>GET <code>/api/customerNotes/{id}</code>  - Retrieve details of a specific customerNote.</li>

<li>POST <code>/api/customerNotes</code>  - Create a new customerNote record.</li>

<li>PUT <code>/api/customerNotes/{id}</code>  - Update an existing customerNote.</li>

<li>DELETE <code>/api/customerNotes/{id}</code>  - Remove a customerNote record.</li>

## Ticket
<li>GET <code>/api/tickets</code>  - Retrieve a list of all tickets.</li>

<li>GET <code>/api/tickets/{id}</code>  - Retrieve details of a specific ticket.</li>

<li>POST <code>/api/tickets</code>  - Create a new ticket record.</li>

<li>PUT <code>/api/tickets/{id}</code>  - Update an existing ticket.</li>

<li>DELETE <code>/api/tickets/{id}</code>  - Remove a ticket record.</li>

## User
<li>GET <code>/api/users</code>  - Retrieve a list of all users.</li>

<li>GET <code>/api/users/{id}</code>  - Retrieve details of a specific user.</li>

<li>POST <code>/api/users</code>  - Create a new user record.</li>

<li>PUT <code>/api/users/{id}</code>  - Update an existing user.</li>

<li>DELETE <code>/api/users/{id}</code> - Remove a user record.</li>


# Project Structure

<li><code>src/main/java/.../controllers</code>: REST API Endpoints.</li>

<li><code>src/main/java/.../models</code>: Database entities.</li>

<li><code>src/main/java/.../repositories</code>: Data access interfaces.</li>

<li><code>src/main/java/.../services</code>: Business logic layer.</li>

<li><code>src/test/java/org.example.crm</code>: Tests</li>
