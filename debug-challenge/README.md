# Spring Boot Scenario-Based Learning (Scenarios 1-15)

This project contains a series of Spring Boot "Scenarios" designed to demonstrate common real-world challenges, interview questions, and production-grade solutions.

## 🚀 How to Run
1. **Clone the repository** (or use the existing folder).
2. **Build the project**: `mvn clean install`
3. **Run the application**: `mvn spring-boot:run`
4. **Base URL**: `http://localhost:8080`

---

## 🛠️ Scenarios Overview

### [Scenario 1: The Invisible Component (@ComponentScan)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/DebugApplication.java)
- **Problem**: Spring fails to find a bean because it's in a package outside the default scan range.
- **Solution**: Explicitly defining `@ComponentScan` or moving the class to a sub-package of the main application class.
- **Test**: Service injection works without `NoSuchBeanDefinitionException`.

### [Scenario 2: The Infinite Mexican Standoff (Circular Dependencies)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/service/CircularServiceA.java)
- **Problem**: Service A depends on B, and B depends on A, causing a startup crash.
- **Solution**: Use `@Lazy` on one of the dependency injections.
- **Test**: Application starts successfully.

### [Scenario 3: The Ghost Transaction (Proxy Self-invocation)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/service/TransactionService.java)
- **Problem**: `@Transactional` doesn't work when a method calls another `@Transactional` method within the same class.
- **Solution**: Self-injection or moving the transactional logic to a separate service.
- **Test**: Rollback occurs correctly during exceptions.

### [Scenario 4: The Selective Filter (Custom Filters)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/filter/CustomHeaderFilter.java)
- **Problem**: Applying filters only to specific URL patterns.
- **Solution**: Using `FilterRegistrationBean` for precise control.
- **Test**: `curl -I http://localhost:8080/api/scenario4/test` (Check for `X-Custom-Header`).

### [Scenario 5: The Leaking Memory (Caching)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/service/CachedService.java)
- **Problem**: Unoptimized data fetching causing high latency.
- **Solution**: Integrated **Caffeine** (L1) and **Redis** (L2) caching.
- **Test**: First request is slow; subsequent requests are near-instant.

### [Scenario 6: Concurrency & Locking](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/service/BankService.java)
- **Problem**: Race conditions during concurrent balance updates.
- **Solution**: Demonstrated **Optimistic Locking** (`@Version`) and **Pessimistic Locking** (`PESSIMISTIC_WRITE`).
- **Test**: Multiple concurrent threads updating the same account.

### [Scenario 7: The Zombie Async Task (@EnableAsync)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/service/AsyncService.java)
- **Problem**: Secondary tasks blocking the main thread or losing security context.
- **Solution**: `@Async` with a custom `ThreadPoolTaskExecutor` and `DelegatingSecurityContextAsyncTaskExecutor`.
- **Test**: API returns immediately while logs show background processing.

### [Scenario 8: JWT & JTI Blacklisting](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/security/JwtUtils.java)
- **Problem**: Standard JWTs cannot be revoked until they expire.
- **Solution**: Use **JTI (JWT ID)** and a Redis blacklist to "kill" tokens on logout.
- **Test**: Login -> Get Token -> Logout -> Try to use token again (Access Denied).

### [Scenario 9: The Chatty Database (N+1 Problem)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/repository/OrderRepository.java)
- **Problem**: Fetching N child records results in N+1 SQL queries.
- **Solution**: Using `JOIN FETCH` in the JPQL query.
- **Test**: Logs show exactly 1 SQL query for child records.

### [Scenario 10: Global Exception Handling](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/exception/GlobalExceptionHandler.java)
- **Problem**: Inconsistent error responses across different parts of the API.
- **Solution**: `@RestControllerAdvice` with a standardized `ErrorResponse` model.
- **Test**: Triggering various errors (400, 404, 500) returns a consistent JSON format.

### [Scenario 11: Conditional Validation (@Validated Groups)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/controller/Scenario11Controller.java)
- **Problem**: Field rules differ between "Create" (Password required) and "Update" (Password optional).
- **Solution**: Using **Validation Groups** (`OnCreate`, `OnUpdate`) with `@Validated`.
- **Test**: POST to `/create` fails without password; PUT to `/update` succeeds.

### [Scenario 12: Soft Delete (@SQLDelete)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/model/SoftDeleteProduct.java)
- **Problem**: Keeping records in DB for history but hiding them from the UI.
- **Solution**: Using `@SQLDelete` to update a flag and `@SQLRestriction` to filter SELECTs.
- **Test**: `findAll()` hides deleted items; Native Query shows them.

### [Scenario 13: Payload Encryption (AttributeConverter)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/util/EncryptionConverter.java)
- **Problem**: Storing sensitive data (SSN, PII) in plain text in the database.
- **Solution**: JPA `AttributeConverter` using AES-128 encryption.
- **Test**: Java sees the clear text; DB logs show encrypted gibberish.

### [Scenario 14: The "Double Spend" Problem (Idempotency)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/java/com/interview/debug/idempotency/IdempotencyAspect.java)
- **Problem**: Duplicate POST requests causing duplicate transactions.
- **Solution**: AOP-based idempotency check using an `Idempotency-Key` header.
- **Test**: Repeat a transaction with the same key; result is replayed, not re-executed.

### [Scenario 15: Database Migration (Flyway)](file:///c:/Users/tushar.seth/Desktop/Scenario%20Based/debug-challenge/src/main/resources/db/migration/V1__init.sql)
- **Problem**: Managing database schema changes manually across different environments.
- **Solution**: Integrated **Flyway** for versioned, automated SQL migrations.
- **Test**: Check `flyway_schema_history` table and `project_data` records.
