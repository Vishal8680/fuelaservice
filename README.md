# Fuel Tax Service (Simplified)

## Project Overview
A small Spring Boot application demonstrating a simplified tax calculation service for fuel shipments across jurisdictions.

[GitHub Repository](https://github.com/Vishal8680/fuelaservice.git)

## Tech Stack
- Java 17
- Spring Boot 2.7.x
- Spring Data JPA
- H2 (testing / default runtime)
- PostgreSQL (production profile)
- Flyway for migrations
- Lombok
- QueryDSL (setup included)

## Prerequisites
- Java 17+ JDK
- Maven 3.6+

## Setup Instructions

## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/Vishal8680/fuelaservice.git
cd fuelaservice


1. Build the project
mvn clean install

2. Run the application
Using Dev Profile (H2 in-memory database)
mvn spring-boot:run
# or
java -jar target/fueltaservice-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

Using Prod Profile (PostgreSQL)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
# or
java -jar target/fueltaservice-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

4. Run Tests
mvn test

API Documentation

All endpoints are prefixed with /api/v1.

## API Endpoints
- POST `/v1/tax/fuel/calculate` - body: `{ "shipmentId":"S1", "jurisdictionCode":"CA", "quantity":100, "pricePerGallon":3.5 }`
- GET `/v1/tax/fuel/history/{shipmentId}` - returns calculation history
- GET `/v1/tax/fuel/jurisdictions` - lists sample jurisdictions

## API Testing via Swagger UI

You can test all APIs using Swagger UI. Make sure the application is running locally.

**Swagger URL:**  
- Dev Profile (H2): `http://localhost:8080/swagger-ui/index.html`  
- Prod Profile (PostgreSQL): `http://localhost:8080/swagger-ui/index.html`

---

### **1. POST /v1/tax/fuel/calculate**
- **Description:** Calculate fuel tax for a shipment.
- **Request Body Example:**
```json
{
  "shipmentId": "SHIP123",
  "jurisdictionCode": "TX01",
  "fuelAmount": 100
}

### **2. GET /v1/tax/fuel/jurisdictions

Description: Retrieve all tax jurisdictions or filter by rate range.

### **3. GET /v1/tax/fuel/history/{shipmentId}

Description: Retrieve fuel tax calculation history for a shipment.

Path Parameter Example:
shipmentId = SHIP123

## Database
Flyway executes `V1__init.sql` on startup to create tables and seed 3 jurisdictions.

## Design Decisions
- Use BigDecimal for monetary calculations
- JPA entities with validation annotations
- QueryDSL included but repository implementation falls back to JPQL for readability in this sample

## Testing
- Unit tests for service layer included (Mockito)
- Data layer tests can be added with @DataJpaTest

## Assumptions
- Tax calculation = quantity * price * baseRate
- Effective jurisdiction determined by latest effectiveDate <= today

