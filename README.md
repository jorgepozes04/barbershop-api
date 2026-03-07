# BarberShop API

![Java](https://img.shields.io/badge/Java-21-orange?style=flat)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen?style=flat)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue?style=flat)
![License](https://img.shields.io/badge/License-MIT-green?style=flat)

A modern and robust RESTful API for barbershop appointment management, built with Spring Boot following industry best practices.

## Overview

BarberShop API is a complete solution for barbershop management offering robust functionality for administration of barbers, clients, services and appointments. The application implements secure JWT authentication, rigorous data validation and follows RESTful standards.

## Features

### Authentication and Security

- JWT token-based authentication
- Role-based authorization (CLIENT, BARBER)
- Spring Security integration
- Custom security filters
- Input validation and sanitization

### Client Management

- Client registration
- Search clients by ID or CPF
- Paginated listing
- Profile updates
- Account deletion

### Barber Management

- Barber registration and management
- Work schedule definition
- Break time configuration
- Service assignment
- Appointment history

### Appointment Management

- Available time slot queries
- Appointment creation
- Reservation cancellation
- Client appointment listing
- Automatic conflict validation

### Service Management

- Service registration
- Price and duration definition
- Barber association
- Available service listing

### Monitoring and Documentation

- Application health checks
- Actuator metrics
- Structured logging
- Swagger/OpenAPI documentation

## Requirements

- Java 21 or higher
- Maven 3.6+ or higher
- MySQL 8.0+ or higher
- Git (optional, for cloning the repository)

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/jorgepozes04/barbershop-api.git
cd barbershop-api
```

### 2. Set up Database

```bash
mysql -u root -p
CREATE DATABASE barbershop_db;
EXIT;
```

### 3. Configure Environment Variables

Create a `.env` file in the project root or set system environment variables:

```properties
# Database
DB_USERNAME=your_mysql_user
DB_PASSWORD=your_mysql_password

# Security
JWT_SECRET=your_secure_secret_key_minimum_32_characters
```

### 4. Install Dependencies

```bash
mvn clean install
```

### 5. Run with Docker Compose (Optional)

```bash
docker-compose up -d
mvn spring-boot:run
```

## Configuration

The application is pre-configured with the following settings in `application.properties`:

```properties
spring.application.name=BarberShop-API
server.port=8081

# Database
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/barbershop_db?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# JWT
api.security.token.secret=${JWT_SECRET}

# Date formatting
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss
```

## Getting Started

### Start the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8081`

### Main Endpoints

#### Authentication

| Method | Endpoint         | Description                |
| ------ | ---------------- | -------------------------- |
| POST   | `/auth/register` | Register new client        |
| POST   | `/auth/login`    | Login and obtain JWT token |

**Example Request (POST /auth/login)**

```json
{
  "username": "client@example.com",
  "password": "password123"
}
```

**Response**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

#### Clients

| Method | Endpoint             | Description                  |
| ------ | -------------------- | ---------------------------- |
| GET    | `/clients`           | List all clients (paginated) |
| GET    | `/clients/{id}`      | Get client by ID             |
| GET    | `/clients/cpf/{cpf}` | Search client by CPF         |
| POST   | `/clients`           | Create new client            |
| PUT    | `/clients/{id}`      | Update client                |
| DELETE | `/clients/{id}`      | Delete client                |

#### Barbers

| Method | Endpoint        | Description         |
| ------ | --------------- | ------------------- |
| GET    | `/barbers`      | List all barbers    |
| GET    | `/barbers/{id}` | Get barber by ID    |
| POST   | `/barbers`      | Register new barber |
| DELETE | `/barbers/{id}` | Delete barber       |

#### Appointments

| Method | Endpoint                                 | Description            |
| ------ | ---------------------------------------- | ---------------------- |
| GET    | `/appointments`                          | List appointments      |
| GET    | `/appointments/{id}`                     | Get appointment by ID  |
| GET    | `/appointments/availability/{barber_id}` | Available time slots   |
| POST   | `/appointments`                          | Create new appointment |
| DELETE | `/appointments/{id}`                     | Cancel appointment     |

#### Services

| Method | Endpoint         | Description        |
| ------ | ---------------- | ------------------ |
| GET    | `/services`      | List all services  |
| GET    | `/services/{id}` | Get service by ID  |
| POST   | `/services`      | Create new service |
| DELETE | `/services/{id}` | Delete service     |

#### Health Check

| Method | Endpoint  | Description        |
| ------ | --------- | ------------------ |
| GET    | `/health` | Application status |

### JWT Authentication

After login, use the token in request headers:

```bash
curl -X GET http://localhost:8081/clients \
  -H "Authorization: Bearer your_jwt_token_here"
```

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=ClientServiceTest
```

### Run Tests with Coverage

```bash
mvn test jacoco:report
```

### Test Suite

The application includes a comprehensive test suite with over 80 unit tests covering:

- Authentication (12 tests)
- Clients (10 tests)
- Barbers (12 tests)
- JWT Tokens (10 tests)
- Appointments (8 tests)
- Work Schedules (11 tests)
- Services (12 tests)

For complete details, see [TESTS.md](TESTS.md)

## Project Structure

```
barbershop-api/
├── src/
│   ├── main/
│   │   ├── java/com/jorgepozes04/barbershop_api/
│   │   │   ├── BarberShopApiApplication.java       # Main class
│   │   │   ├── config/                             # Configuration
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── SecurityFilter.java
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/                         # REST Controllers
│   │   │   │   ├── AppointmentController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BarberController.java
│   │   │   │   ├── ClientController.java
│   │   │   │   └── ServiceOfferedController.java
│   │   │   ├── service/                            # Business Logic
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── AppointmentService.java
│   │   │   │   ├── ClientService.java
│   │   │   │   └── TokenService.java
│   │   │   ├── repository/                         # Data Access
│   │   │   │   ├── AppointmentRepository.java
│   │   │   │   ├── ClientRepository.java
│   │   │   │   └── BarberRepository.java
│   │   │   ├── entity/                             # JPA Models
│   │   │   │   ├── Client.java
│   │   │   │   ├── Barber.java
│   │   │   │   ├── Appointment.java
│   │   │   │   └── ServiceOffered.java
│   │   │   ├── dto/                                # Data Transfer Objects
│   │   │   │   ├── ClientDTO.java
│   │   │   │   ├── BarberDTO.java
│   │   │   │   └── AppointmentDTO.java
│   │   │   ├── exception/                          # Exception Handling
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── ValidationException.java
│   │   │   └── enums/                              # Enumerations
│   │   │       ├── Role.java
│   │   │       ├── Status.java
│   │   │       └── Day.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/jorgepozes04/barbershop_api/
│           ├── service/                            # Service tests
│           ├── controller/                         # Controller tests
│           └── exception/                          # Exception tests
├── pom.xml                                          # Maven dependencies
├── docker-compose.yml                               # Docker Compose
├── mvnw                                             # Maven Wrapper (Unix)
├── mvnw.cmd                                         # Maven Wrapper (Windows)
├── TESTS.md                                         # Test documentation
└── README.md                                        # This file
```

## Technologies

### Backend

- Java 21 - Programming language
- Spring Boot 4.0.3 - Main framework
- Spring Data JPA - Data access
- Spring Security - Authentication and authorization
- MySQL 8.0+ - Relational database

### Security

- JWT (JSON Web Tokens) - Stateless authentication
- Auth0 Java JWT - Token manipulation library
- Spring Security - Authorization and filters

### Tools and Libraries

- Lombok - Boilerplate reduction
- Hibernate - ORM
- Maven - Dependency manager
- JUnit 5 - Testing framework

### Documentation

- Swagger/OpenAPI - Interactive API documentation

## Business Rules

### Clients

- CPF must be unique
- Minimum password of 6 characters
- Email validation required

### Barbers

- CPF is unique and required
- Username must be unique
- Minimum password of 8 characters
- Only barbers can be assigned to appointments

### Appointments

- Time slots respect barber work schedule
- No appointment overlaps allowed
- Duration based on selected service
- Clients can only cancel future appointments

### Work Schedules

- Work time must be greater than break time
- Multiple schedules per day of the week
- Date validation required

## Contributing

Contributions are welcome! To contribute:

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Guidelines

- Follow the existing code style
- Add tests for new features
- Update documentation as needed
- Follow [Conventional Commits](https://www.conventionalcommits.org/)

## Reporting Issues

Found a bug? Please open an issue with:

- Clear description of the problem
- Steps to reproduce
- Expected and actual behavior
- Screenshots (if applicable)

## Roadmap

- Payment system integration
- Email/SMS notifications
- Analytics dashboard
- Mobile app (Flutter)
- Multi-barbershop support
- Review and ratings system
- Google Calendar integration

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

Jorge Pozes

- GitHub: [@jorgepozes04](https://github.com/jorgepozes04)
- Email: jorgepozes04@example.com

## Acknowledgments

- Spring Boot team
- Open source community
- All contributors

---

Made with care by Jorge Pozes
