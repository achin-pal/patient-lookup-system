# Patient Lookup - CarePlus

CarePlus is a full-stack patient lookup and management application built with Spring Boot, PostgreSQL, and React.

The application provides secure patient search and management features using JWT authentication and role-based access control.

## Features

* JWT-based authentication
* Role-based access control
* Patient search by first or last name
* Server-side patient pagination
* View patient details
* Create patient records
* Update patient records
* Delete patient records
* Duplicate email validation
* Employee self-registration
* Administrator user access management
* Administrators cannot change their own access level
* Swagger / OpenAPI documentation
* Light and dark mode
* Application logging
* Centralized exception handling

## Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* Swagger / OpenAPI
* Lombok
* Maven

### Frontend

* React
* Vite
* Axios
* Bootstrap
* JavaScript

## Test Accounts

The following accounts are available for testing.

### Administrator

```text
Username: admin
Password: admin123
```

### Employee

```text
Username: employee
Password: employee123
```

These accounts are intended for demonstration and assessment purposes.

## Role-Based Access

| Feature                | Administrator | Employee |
| ---------------------- | ------------- | -------- |
| View patients          | Yes           | Yes      |
| Search patients        | Yes           | Yes      |
| View patient details   | Yes           | Yes      |
| Create patients        | Yes           | No       |
| Update patients        | Yes           | No       |
| Delete patients        | Yes           | No       |
| View application users | Yes           | No       |
| Change user roles      | Yes           | No       |

Administrators cannot change their own access level.

Public signup always creates an account with the `EMPLOYEE` role.

## Prerequisites

Before running the application, install:

* Java 17
* Maven
* Node.js
* npm
* PostgreSQL

## Database Setup

Create a PostgreSQL database named:

```text
patientdb
```

The application is configured to connect to PostgreSQL on:

```text
Host: localhost
Port: 5432
Database: patientdb
```

The database scripts used by Spring Boot are located at:

```text
backend/src/main/resources/schema.sql
backend/src/main/resources/data.sql
```

`schema.sql` creates the required database tables and indexes.

`data.sql` contains the application seed data.

## Running the Backend

Navigate to the backend directory:

```bash
cd backend
```

Run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The backend runs at:

```text
http://localhost:8080
```

## Running the Frontend

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the Vite development server:

```bash
npm run dev
```

The frontend runs at:

```text
http://localhost:5173
```

## Frontend Environment Configuration

An example frontend environment file is included:

```text
frontend/.env.example
```

It contains:

```env
VITE_API_URL=http://localhost:8080/api
```

The application also uses `http://localhost:8080/api` as its default API URL when the environment variable is not provided.

If a custom frontend environment configuration is needed, copy:

```text
frontend/.env.example
```

to:

```text
frontend/.env
```

and update the API URL as required.

## Swagger / OpenAPI

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

Protected endpoints require JWT authentication.

To test secured endpoints in Swagger:

1. Call the login endpoint using one of the test accounts.
2. Copy the JWT token returned by the login request.
3. Click **Authorize** in Swagger UI.
4. Enter the JWT token.
5. Test the protected API endpoints.

## API Endpoints

### Authentication

```text
POST /api/auth/login
POST /api/auth/signup
```

### Patients

```text
GET    /api/patients
GET    /api/patients/{id}
POST   /api/patients
PUT    /api/patients/{id}
DELETE /api/patients/{id}
```

The patient list endpoint supports pagination and optional name search.

Example:

```text
GET /api/patients?name=Smith&page=0&size=5
```

A valid sort can also be supplied:

```text
GET /api/patients?page=0&size=5&sort=patientId,asc
```

### User Administration

```text
GET /api/users
PUT /api/users/{id}/role
```

These endpoints are restricted to users with the `ADMIN` role.

## Patient Search and Pagination

The patient listing uses server-side pagination.

The default page size is:

```text
5
```

The application displays the total number of matching patient records at the top of the patient list.

Pagination controls allow navigation between pages.

Patient name search works together with pagination.

## Validation and Error Handling

The application uses centralized exception handling for common API errors.

Handled scenarios include:

* Patient not found
* User not found
* Invalid request data
* Duplicate patient email
* Unauthorized access
* Forbidden operations
* Unexpected server errors

Duplicate patient emails return a meaningful error instead of a generic server error.

## Security

The application uses Spring Security and JWT authentication.

Passwords are encoded before being stored.

Protected API endpoints require a valid JWT token.

### Administrator

Administrators have full patient management access and can manage application user roles.

### Employee

Employees have read-only patient access and can:

* View patients
* Search patients
* View patient details

Employees cannot create, update, or delete patient records.

## Logging

The backend uses SLF4J logging.

Logging is included for:

* Patient API requests
* Patient service operations
* Authentication activity
* User access management
* Validation failures
* Not-found exceptions
* Unexpected application errors

Sensitive values such as passwords and JWT tokens are not written to application logs.

## Project Structure

```text
patient-lookup-app/
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │           ├── schema.sql
│   │           └── data.sql
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   ├── .env.example
│   ├── package.json
│   └── package-lock.json
│
└── README.md
```

## Additional Features

In addition to the core patient CRUD requirements, the project includes:

* JWT authentication
* Role-based authorization
* Employee signup
* Administrator user management
* Self-role-change protection for administrators
* Server-side pagination
* Live patient search
* Duplicate email protection
* Swagger authentication support
* Centralized exception handling
* Application logging
* Responsive user interface
* Persistent light and dark themes

## Application URLs

### Frontend

```text
http://localhost:5173
```

### Backend

```text
http://localhost:8080
```

### Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

## Notes

This project demonstrates:

* REST API development with Spring Boot
* PostgreSQL database integration
* Spring Data JPA
* JWT authentication
* Role-based access control
* React frontend development
* API documentation with Swagger / OpenAPI
* Validation and exception handling
* Pagination and search
* Application logging
* Frontend theme management
