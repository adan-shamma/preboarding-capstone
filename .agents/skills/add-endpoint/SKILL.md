---
name: add-endpoint
description: Adds a new REST endpoint to this Spring Boot project with tests. Use when asked to add or change an API endpoint.
---

# Add an endpoint

1. Read the existing controller and tests to match conventions.
2. Write a MockMvc test for the new endpoint first, including error cases. Run the test and confirm it fails before implementing the endpoint. Do not implement the endpoint before this test is written and run.
3. Implement the endpoint. Keep business logic out of the controller.
4. Run ./mvnw test and fix failures until all pass.
5. Summarize the change and list the files you touched.
6. Keep the code simple and readable. Avoid changing unrelated parts of the project.
