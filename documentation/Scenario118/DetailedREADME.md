# Testing Strategies in Spring Boot — Complete Interview Reference

## Table of Contents
1. [The Testing Pyramid](#1-testing-pyramid)
2. [Unit Testing with JUnit 5 & Mockito](#2-unit-tests)
3. [Slicing Tests with @WebMvcTest](#3-webmvc-test)
4. [Full Integration Testing with @SpringBootTest](#4-springboot-test)
5. [The Classic Interview Trap: Mocking vs Spying](#5-the-classic-interview-trap-mocking)
6. [Testing Data Access with @DataJpaTest](#6-datajpa-test)
7. [Mocking External APIs with MockRestServiceServer](#7-mock-rest)
8. [Using TestContainers for Real DB Testing](#8-test-containers)
9. [Verification vs Assertion](#9-verification)
10. [Handling Security in Tests (@WithMockUser)](#10-security-tests)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. The Testing Pyramid
A healthy test suite follows the pyramid:
- **Unit Tests**: (Base) Fast, isolated, hundreds/thousands of them.
- **Integration Tests**: (Middle) Test interaction between components.
- **E2E/System Tests**: (Peak) Test the whole flow, slow, few of them.

---

## 2. Unit Testing
Focuses on a single class. Dependencies are mocked using Mockito. No Spring context is loaded.
**Example**: Testing business logic in a `@Service` by mocking the `@Repository`.

---

## 3. @WebMvcTest
A "Slice Test". It only loads the web layer (Controllers, Interceptors, Filters) and mocks the service layer.
**Benefit**: Much faster than a full integration test because it doesn't start JPA or the full application context.

---

## 4. @SpringBootTest
Loads the **entire** application context. Used for testing the integration of all layers (Controller -> Service -> Repository -> DB).
- `webEnvironment = WebEnvironment.RANDOM_PORT`: Starts a real server.
- `webEnvironment = WebEnvironment.MOCK`: Simulates the server using MockMvc.

---

## 5. The Classic Interview Trap: @Mock vs @MockBean
**The Trap**: A developer uses `@Mock` inside an integration test.
**The Problem**: `@Mock` is a pure Mockito annotation. It creates a mock object but doesn't register it in the Spring context. The Spring context will still try to inject the "real" bean into other components.
**The Fix**: Use **@MockBean**. It creates a Mockito mock and puts it into the Spring Application Context, replacing any existing bean of the same type.

---

## 6. @DataJpaTest
Another slice test. It configures an in-memory database (like H2), scans for `@Entity` classes, and configures Spring Data JPA repositories. It is transactional and rolls back after every test.

---

## 7. MockRestServiceServer
When your application calls an external REST API, you shouldn't use a mock service. Instead, you should use `MockRestServiceServer` to intercept the outgoing HTTP request and return a simulated response.

---

## 8. TestContainers
The modern standard for integration testing. It uses Docker to spin up a real instance of PostgreSQL, MySQL, or Kafka during the test run, ensuring your tests are as close to production as possible.

---

## 9. Verification
Mockito's `verify(mock).method()` is used to ensure that a method was actually called with the expected arguments. This is different from `assertEquals()`, which checks the state of a returned value.

---

## 10. Security Testing
Use `@WithMockUser(roles = "ADMIN")` to simulate a logged-in user without having to go through the actual login process in every test.

---

## 11. Common Mistakes
1. Loading the full Spring context for simple logic that could be a unit test.
2. Not cleaning up the database between integration tests (tests become order-dependent).
3. Over-mocking (Mocking the very component you are trying to test).

---

## 12. Quick-Fire Interview Q&A
**Q: What is the benefit of @WebMvcTest?**  
A: It's fast and allows you to test controller mappings, JSON serialization, and validation without loading the database.  
**Q: What is a "Spy"?**  
A: A spy is a partial mock. It calls the real methods of an object unless you explicitly tell it to return a mocked value for a specific call.
