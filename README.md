# CRM Monorepo Project

This is a full-stack CRM application implemented using a monorepo structure. The project contains separate directories for backend and frontend components.

# HOX

Screenshots found in the frontend directorys Readme.md

## Project Structure

* backend/: Spring Boot (Java 21, Maven) application providing a REST API and connecting to a PostgreSQL database.
* frontend/: Vite + React application for the user interface.

## Backend (Spring Boot)

The backend manages company data and other CRM functionalities.

### Prerequisites
* Java 17 (OpenJDK)
* Maven
* PostgreSQL database

### Getting Started
1. Navigate to the backend directory:
   cd backend
2. Run the application:
   ./mvnw spring-boot:run

The backend runs by default at http://localhost:8080.

## Frontend (React + Vite)

The frontend handles the user interface and data visualization.

### Prerequisites
* Node.js
* npm

### Getting Started
1. Navigate to the frontend directory:
   cd frontend
2. Install dependencies (first time only):
   npm install
3. Start the development server:
   npm run dev

The frontend runs by default at http://localhost:5173.

## Current Project Status

1. Project successfully migrated to a monorepo structure.
2. Git version control initialized at the root level to track both backend and frontend.
3. Backend is tested and successfully connected to the PostgreSQL database.
4. CORS settings are configured in the backend to allow requests from the local frontend origin.
5. IntelliJ IDEA is configured to manage the entire project root.
