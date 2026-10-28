# BookMyShow — Movie Ticket Booking Backend

A movie ticket booking backend built using Java, Spring Boot, Spring Data JPA, and MySQL. The project provides REST APIs for user authentication, movie and show listing, seat availability, and ticket booking.

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Security:** Spring Security, BCrypt password hashing, JWT authentication
- **Build Tool:** Maven
- **Testing:** JUnit 5, Mockito

## Features

- User registration with password hashing using BCrypt
- User login with JWT token generation
- Protected REST API endpoints using JWT authentication
- Movie listing
- Show listing with movie, screen, and theatre details
- Seat listing for a specific show
- Ticket booking with seat availability validation
- Transaction management and pessimistic locking for concurrent seat booking

## Project Structure

```text
src/main/java/com/scaler/bookMyShow/
├── config/          # Security and application configuration
├── controller/      # REST API controllers
├── dto/             # Request and response objects
├── model/           # JPA entities
│   └── enums/       # Application enums
├── Repository/      # Spring Data JPA repositories
└── service/         # Business logic
```

## Prerequisites

Install the following:

- Java (the version configured in `pom.xml`)
- Maven
- MySQL Server
- IntelliJ IDEA or another Java IDE

## Setup and Installation

### 1. Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd BookMyShow2
```

Replace the repository URL with your public GitHub repository URL.

### 2. Create the database

Open MySQL and execute:

```sql
CREATE DATABASE bookmyshow;
```

### 3. Configure database credentials

Open `src/main/resources/application.properties` and configure your MySQL connection:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookmyshow
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

Set `DB_USERNAME` and `DB_PASSWORD` in your environment or IntelliJ run configuration. Keep database credentials and JWT secrets out of version control.

Retain any other configuration properties already required by your application, including JWT settings.

### 4. Run the application

Open the project in IntelliJ IDEA, allow Maven dependencies to load, and run `BookMyShow2Application`.

The application runs at:

```text
http://localhost:8080
```

## API Endpoints

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| POST | `/users` | Register a user | Not required |
| POST | `/users/login` | Log in and obtain a JWT | Not required |
| GET | `/movies` | Retrieve movies | JWT |
| GET | `/shows` | Retrieve shows | JWT |
| GET | `/shows/{showId}/seats` | Retrieve seats for a show | JWT |
| POST | `/bookings?showId={showId}` | Create a booking | JWT |
| GET | `/bookings` | Retrieve the logged-in user's bookings | JWT |

Authentication requirements should be checked against the current security configuration if any endpoint behavior changes.

## Example API Requests

### Register a user

```http
POST /users
Content-Type: application/json
```

```json
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "Test@12345"
}
```

### Log in

```http
POST /users/login
Content-Type: application/json
```

```json
{
  "email": "test@example.com",
  "password": "Test@12345"
}
```

The response includes a JWT token. Use it in the `Authorization` header for protected endpoints:

```http
Authorization: Bearer <YOUR_JWT_TOKEN>
```

### Get all movies

```http
GET /movies
Authorization: Bearer <YOUR_JWT_TOKEN>
```

### Get all shows

```http
GET /shows
Authorization: Bearer <YOUR_JWT_TOKEN>
```

### Get seats for a show

```http
GET /shows/1/seats
Authorization: Bearer <YOUR_JWT_TOKEN>
```

### Create a booking

```http
POST /bookings?showId=1
Authorization: Bearer <YOUR_JWT_TOKEN>
Content-Type: application/json
```

Example request body:

```json
[1]
```

The example requests assume the referenced movie, show, and seat records exist in the database and that the seat is eligible for booking.

## Concurrency Handling

The booking service uses transaction management and pessimistic database locking when accessing a seat for booking. This helps prevent concurrent requests from booking the same seat simultaneously.

## Running Tests

Run the test suite from IntelliJ IDEA using the test runner, or execute:

```bash
mvn test
```

## Future Improvements

- Complete payment processing and booking confirmation
- Implement booking cancellation and expiry handling
- Add comprehensive integration tests
- Improve API documentation with OpenAPI/Swagger
- Add validation and more consistent error responses
- Add deployment configuration and Docker support


## Challenges and Learnings

During development, I worked through several practical backend development challenges:

- **JWT Authentication:** Configured Spring Security to protect API endpoints and tested authenticated requests using JWT bearer tokens.
- **Password Security:** Implemented BCrypt password hashing during user registration and password verification during login.
- **Database Integration:** Connected the Spring Boot application to MySQL and worked with JPA entities and repositories to retrieve application data.
- **Concurrent Seat Booking:** Used transaction management and pessimistic locking to help prevent multiple requests from booking the same seat concurrently.
- **REST API Testing:** Tested registration, login, movie listing, show listing, seat retrieval, and booking endpoints using HTTP requests.
- **Circular JSON Serialization:** Identified and resolved recursive JSON responses caused by bidirectional entity relationships by configuring Jackson serialization appropriately.
- **Exception Handling:** Worked on handling application errors and returning appropriate HTTP responses.

## Key Learnings

This project helped me understand how Spring Boot controllers, services, repositories, JPA entities, database transactions, and Spring Security work together in a backend application. It also gave me practical experience debugging API responses and validating functionality through testing.


## Disclaimer

This project is intended for learning and demonstrating backend development concepts. It is not a production-ready ticket booking platform.
