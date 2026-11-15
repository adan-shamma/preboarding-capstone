# Project Instructions

## Project
This project is a small Spring Boot REST service for managing tasks  without using database

## Build and Test
To run the tests use: .\mvnw.cmd test

## Conventions
- Controllers should handle only  HTTP requests and responses.
- Logics and operations should be handled in the service class and not in the Controller.
- Every new endpoint must have a MockMvc test.
- Write simple readable, and maintainable code.
- Use clear and meaningful names for variables and methods.
- Avoid unnecessary code duplication.
- 
## Rules
- Always run tests after every change and before returning task is complete.
- After every change, explain what was updated, how it affects the system, and whether the tests passed.
- Return appropriate HTTP status codes and clear error messages.
- Validate user input before processing or saving it.