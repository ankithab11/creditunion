# Credit Union Member Account API

## Description
This application is a RESTful API for managing credit union member accounts, built with Spring Boot 3.

## Requirements
- Java 17+
- Maven 3.6+

## Setup
Used IntelliJ IDE for development.
H2 in-memory database is used to storing and manipulating user data.
### Build and Run

1. Open the Source code in IDE:

2. Navigate to the project directory:
   `
3. Build the application:
    ```sh
    mvn clean install
    ```
4. Run the application:
    ```sh
    mvn spring-boot:run

### API Endpoints
- GET `/api/v1/accounts`: Retrieve all accounts
- GET `/api/v1/accounts/{id}`: Retrieve account by ID
- POST `/api/v1/accounts`: Create a new account
- PUT `/api/v1/accounts/{id}`: Update an existing account
- DELETE `/api/v1/accounts/{id}`: Delete an account
- SEARCH `/api/v1/accounts/search?id={id}`: Search account using id
- SEARCH `/api/v1/accounts/search?name={name}`: Search account using name

## Design
- The application uses an in-memory H2 database for simplicity.
- The package structure is based on features to enhance modularity and maintainability.
- Basic error handling is implemented using a global exception handler.

## Testing and Code coverage
- Tests are included for service and controller layers to ensure the correctness of the application.
- Included Jacoco to check code coverage and to test code quality.
1. Build the application unittests:

   mvn clean test

2. To generate the Jacoco report:

   mvn jacoco:report

## Jakarta EE Specifications
- The application adheres to Jakarta EE specifications by using `jakarta.persistence` for entity definitions and `jakarta.transaction.Transactional` for transaction management.
