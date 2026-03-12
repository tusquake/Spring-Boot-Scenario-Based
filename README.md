# 🎓 Spring Boot Mastering: Scenarios 1-15 🚀

This project is a comprehensive guide to mastering Spring Boot through real-world scenarios. Each scenario addresses a common production issue, architectural challenge, or high-frequency interview topic.

## 📥 Getting Started
- **Java**: 17+
- **Build Tool**: Maven
- **Database**: H2 (In-Memory)
- **Port**: 8080 (Configured in `application.properties`)

---

## 🏗️ Detailed Scenario Guides

### 1️⃣ Scenario 1: The Invisible Component
*   **Concept**: Bean Discovery and `@ComponentScan`.
*   **The Problem**: A service in `com.other.package` is not injected because Spring only scans sub-packages of the `@SpringBootApplication` class.
*   **Solution**: Added explicit `@ComponentScan(basePackages = {"com.interview.debug", "com.other.package"})`.
*   **Interview Tip**: Mention that by default, Spring scans the package of the class annotated with `@SpringBootApplication` and all its sub-packages.

---

### 2️⃣ Scenario 2: Circular Dependency Standoff
*   **Concept**: Bean Lifecycle and Dependency Resolution.
*   **The Problem**: `ServiceA` needs `ServiceB`, and `ServiceB` needs `ServiceA`. Spring fails to initialize either.
*   **Solution**: Annotated one of the constructor parameters or fields with `@Lazy`.
*   **Interview Tip**: In Spring Boot 2.6+, circular dependencies are disabled by default. `@Lazy` creates a proxy that is replaced with the real bean only when first called.

---

### 3️⃣ Scenario 3: Transactional Ghosting
*   **Concept**: AOP Proxies and Self-invocation.
*   **The Problem**: `@Transactional` fails when a method inside a class calls another transactional method in the *same* class (bypass proxy).
*   **Solution**: Used self-injection or refactored logic into a separate service.
*   **Test**: `curl -X POST http://localhost:8080/api/scenario3/test` (Verify DB state after exception).

---

### 4️⃣ Scenario 4: The Selective Filter
*   **Concept**: Servlet Filters vs Spring Security.
*   **The Problem**: Applying a specific HTTP header/log logic only to `/api/scenario4/*`.
*   **Solution**: Registered `FilterRegistrationBean` to define URL patterns manually.
*   **Test**: `curl -I http://localhost:8080/api/scenario4/test` -> Observe `X-Custom-Header`.

---

### 5️⃣ Scenario 5: Multi-Level Caching (L1 & L2)
*   **Concept**: Cache-Aside Pattern.
*   **The Problem**: Database is overwhelmed with repetitive queries for static data.
*   **Solution**: Combined **Caffeine** (In-memory L1) with **Redis** (Distributed L2).
*   **Verification**: First request (DB hit), Second (Caffeine hit - ultra fast), Restart App (Redis hit - still fast).

---

### 6️⃣ Scenario 6: Concurrency & Database Locking
*   **Concept**: Data Integrity in High-Traffic Apps.
*   **The Problem**: Two users update a balance at the same time; one update is lost.
*   **Solution**: 
    - **Optimistic**: `@Version` column (Throw `ObjectOptimisticLockingFailureException`).
    - **Pessimistic**: `@Lock(LockModeType.PESSIMISTIC_WRITE)` (DB level row lock).

---

### 7️⃣ Scenario 7: Async Security Context Propagation
*   **Concept**: Thread-local variables vs Async processing.
*   **The Problem**: `@Async` methods lose the `SecurityContext` (User info) because they run on a different thread.
*   **Solution**: Use `DelegatingSecurityContextAsyncTaskExecutor` to copy the context to the new thread.
*   **Test**: Logged user name inside an async method.

---

### 8️⃣ Scenario 8: JWT Revocation (JTI Blacklisting)
*   **Concept**: Stateless vs Stateful security components.
*   **The Problem**: JWTs are valid until expiry, even after a user logs out.
*   **Solution**: Store a unique **JTI (JWT ID)** in Redis upon logout. Check this blacklist in `JwtAuthenticationFilter`.
*   **Test**: Login -> Get Token -> `/logout` -> Try `/api/secure` with old token (returns 401).

---

### 9️⃣ Scenario 9: The N+1 Query Disaster
*   **Concept**: Hibernate Fetching Strategies.
*   **The Problem**: Fetching 10 Users and their Orders triggers 11 SQL queries.
*   **Solution**: Changed Repository query to use `JOIN FETCH u.orders`.
*   **Check**: Console logs show exactly **one** SQL SELECT with a JOIN.

---

### 🔟 Scenario 10: Standardized Exception Handling
*   **Concept**: API Contract and Global Advice.
*   **The Problem**: Error responses vary (sometimes HTML, sometimes plain text, sometimes generic Spring JSON).
*   **Solution**: Created `GlobalExceptionHandler` with `@RestControllerAdvice`.
*   **Test**: `curl http://localhost:8080/api/scenario10/notfound` -> Uniform JSON response with timestamp.

---

### 1️⃣1️⃣ Scenario 11: Validation Groups (OnCreate vs OnUpdate)
*   **Concept**: Jakarta Bean Validation.
*   **The Problem**: 'Password' is mandatory for Registration but must be ignored for 'Update Profile' requests.
*   **Solution**: Used marker interfaces `OnCreate` and `OnUpdate` with `@Validated({Group.class, Default.class})`.

---

### 1️⃣2️⃣ Scenario 12: Soft Deletes with Hibernate
*   **Concept**: Audit Trails and Data Preservation.
*   **The Problem**: `repository.delete()` removes data permanently.
*   **Solution**:
    - `@SQLDelete(sql = "UPDATE ... SET deleted = true ...")`
    - `@SQLRestriction("deleted = false")`
*   **Verification**: `SELECT *` via Native Query shows the hidden deleted rows.

---

### 1️⃣3️⃣ Scenario 13: Transparent Field Encryption
*   **Concept**: Data Security at Rest.
*   **The Problem**: PII (SSN, Email) is stored as plain text in the database.
*   **Solution**: Implemented `AttributeConverter<String, String>` using AES-128.
*   **Result**: 
    - `user.getSsn()` -> `"123-45"`
    - DB Column -> `"A9B2...Gibberish"`

---

### 1️⃣4️⃣ Scenario 14: API Idempotency (The Double-Spend Fix)
*   **Concept**: Distributed Systems & Network Retries.
*   **The Problem**: Retrying a payment POST request charges the customer twice.
*   **Solution**: Used an `Idempotency-Key` header with an AOP Aspect to cache and replay the first response.

---

### 1️⃣5️⃣ Scenario 15: Schema Management with Flyway
*   **Concept**: Version Control for Databases.
*   **The Problem**: Manual SQL scripts are hard to sync across dev/staging/prod.
*   **Solution**: Use `V1__init.sql` in `db/migration`. Flyway runs it automatically at startup.
*   **Test**: Check the `flyway_schema_history` table in the H2 console.

---

### 1️⃣6️⃣ Scenario 16: Graceful Shutdown
*   **Concept**: Application Lifecycle Management.
*   **The Problem**: Killing a Spring Boot app while a user is performing a long-running task (e.g., file upload or report generation) causes an immediate connection reset and potential data corruption.
*   **Solution**: 
    - Set `server.shutdown=graceful` in `application.properties`.
    - Configured `spring.lifecycle.timeout-per-shutdown-phase=30s` to allow a buffer for active requests to finish.
*   **Verification**: 
    1. Call `GET /api/scenario16/long-process`.
    2. Stop the application (Ctrl+C). 
    3. Observe that the app waits 15 seconds for the request to complete before actually terminating.

---

### 1️⃣7️⃣ Scenario 17: Log Rotation & Logback
*   **Concept**: Observability and Resource Management.
*   **The Problem**: A single `app.log` file grows indefinitely, eventually crashing the server by consuming 100% disk space.
*   **Solution**: 
    - Implemented `logback-spring.xml`.
    - Configured `SizeAndTimeBasedRollingPolicy`.
    - **Retention**: Max 10MB per file, Max 30 days history, Max 1GB total cap.
*   **Test**: Call `/api/scenario17/generate-logs` and check the `logs/` directory.

---

### 1️⃣8️⃣ Scenario 18: Custom Spring Boot Starter
*   **Concept**: Modularity and Auto-Configuration.
*   **The Problem**: Code duplication across multiple microservices for common features (logging, security, etc.).
*   **Solution**: 
    - Created an `AutoConfiguration` class.
    - Used `@ConditionalOnProperty` to make the feature optional.
    - Registered the configuration in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
*   **Test**: Call `/api/scenario18/check-starter`. Check the console for a stylized banner.

---

### 2️⃣2️⃣ Scenario 22: API Rate Limiting (Bucket4j)
*   **Concept**: Protection against DDoS and API Abuse.
*   **The Problem**: A malicious user or a buggy script calls your API 1,000 times per second, crashing the database or costing you money on third-party APIs.
*   **Solution**: 
    - Implemented **Bucket4j** library.
    - Configured a **Token Bucket** strategy (e.g., 5 requests per minute).
    - Added a `RateLimitingFilter` to intercept requests and return `429 Too Many Requests`.
*   **Test**: 
    1. Call `curl -I http://localhost:8080/api/scenario22/test-limit`.
    2. Repeat the command 6 times quickly.
    3. Observe the `429` status and `X-Rate-Limit-Remaining` header.

---

### 2️⃣3️⃣ Scenario 23: Circuit Breaker Pattern (Resilience4j)
*   **Concept**: Fault Tolerance and Cascading Failure Prevention.
*   **The Problem**: A downstream API (e.g., a payment gateway or weather service) is slow or failing. Your application threads get blocked waiting for it, eventually running out of resources and crashing.
*   **Solution**: 
    - Implement **Resilience4j CircuitBreaker**.
    - Define thresholds for failure rates and slow calls.
    - Provide a **Fallback** method to return cached or default data when the circuit is OPEN.
*   **Test**: 
    1. Call `/api/scenario23/unstable-call`.
    2. Repeatedly trigger failures (e.g., via a param or by simulating a crash).
    3. Observe the state transition to `OPEN` and the fallback response.

---

### 2️⃣4️⃣ Scenario 24: Distributed Tracing (Micrometer Tracing)
*   **Concept**: Observability and Log Correlation.
*   **The Problem**: How do you track a single request's journey through a complex system of microservices? If an error occurs, how do you find all related logs across different logs?
*   **Solution**: 
    - Implement **Micrometer Tracing** with a **Brave** bridge.
    - Observe how every request is automatically assigned a `traceId` and `spanId`.
    - Configure logging to include these IDs in the MDC (Mapped Diagnostic Context).
*   **Test**: 
    1. Call `/api/scenario24/trace-me`.
    2. Check the logs (both console and `app.log`).
    3. Verify that the `traceId` is consistent across all log statements for that request.

---

### 2️⃣5️⃣ Scenario 25: API Versioning Strategies
*   **Concept**: API Evolution and Compatibility.
*   **The Problem**: How do you introduce breaking changes to an API without breaking existing clients?
*   **Solution**: Implement three versioning strategies:
    1.  **URL Versioning**: `/api/v1/data`
    2.  **Custom Header Versioning**: `X-API-VERSION: 2`
    3.  **Media Type (Content Negotiation)**: `Accept: application/vnd.company.v3+json`
*   **Test**: 
    1. Use `curl` with different URLs, headers, and Accept values to see the different responses.
