# Spring Boot Interview Preparation Hub: The Debug Challenge

This repository is a comprehensive guide to Spring Boot and Spring Security interview scenarios. Each scenario is implemented as a "Debug Challenge" to help you understand core concepts, common pitfalls, and architectural patterns.

---

## 🚀 Scenario Index

| Scenario | Topic | Key Concepts |
| :--- | :--- | :--- |
| **01** | [Bean Lifecycle](documentation/Scenario1/README.md) | `@PostConstruct`, `@PreDestroy`, Bean Initialization |
| **02** | [Bean Scopes Masterclass](documentation/Scenario2/README.md) | Singleton vs Prototype, Injection Pitfalls |
| **03** | [Injection Patterns](documentation/Scenario3/README.md) | Constructor vs Field vs Setter, Best Practices |
| **04** | [Qualifiers & Primary](documentation/Scenario4/README.md) | Disambiguation, Handling Multiple Beans |
| **05** | [Circular Dependencies](documentation/Scenario5/README.md) | Injection patterns, `@Lazy` resolution |
| **06** | [Concurrency Control](documentation/Scenario6/README.md) | Race Conditions, Pessimistic vs Optimistic Locking |
| **07** | [Async Processing](documentation/Scenario7/README.md) | `@Async`, SecurityContext propagation |
| **08** | [JWT Auth](documentation/Scenario8/README.md) | Token generation, validation, blacklisting |
| **09** | [JPA N+1 Problem](documentation/Scenario9/README.md) | `JOIN FETCH`, Entity Graphs |
| **10** | [Validation logic](documentation/Scenario10/README.md) | Custom `@Constraint`, Hibernate Validator |
| **11** | [Global Exception Handling](documentation/Scenario11/README.md) | `@ControllerAdvice`, `ProblemDetail` |
| **12** | [Spring Events](documentation/Scenario12/README.md) | `@EventListener`, Async Events |
| **13** | [Caching (Redis)](documentation/Scenario13/README.md) | `@Cacheable`, TTL, Cache Eviction |
| **14** | [Idempotency](documentation/Scenario14/README.md) | Idempotent keys, Distributed Locks |
| **15** | [Rate Limiting](documentation/Scenario15/README.md) | Bucket4j, API Throtelling |
| **16** | [Profiles & Config](documentation/Scenario16/README.md) | `@Profile`, Conditional Beans |
| **17** | [AOP Introduction](documentation/Scenario17/README.md) | Aspect, Pointcuts, Advices |
| **18** | [Transaction Propagation](documentation/Scenario18/README.md) | `REQUIRED`, `REQUIRES_NEW`, Rollback rules |
| **19** | [API Version Lifecycle](documentation/Scenario19/README.md) | Current, Deprecated, Sunset, Removed (410 Gone) |
| **20** | [Custom @PropertySource](documentation/Scenario20/README.md) | Externalized Config, Environment Abstraction |
| **22** | [Custom Annotations](documentation/Scenario22/README.md) | Creating custom AOP-driven decorators |
| **23** | [Pagination & Sorting](documentation/Scenario23/README.md) | `Pageable`, `Sort`, HATEOAS basics |
| **24** | [Content Negotiation](documentation/Scenario24/README.md) | JSON vs XML, Accept headers |
| **25** | [Observability](documentation/Scenario25/README.md) | Micrometer, Tracing, Spans |
| **26** | [CORS Deep Dive](documentation/Scenario26/README.md) | Preflight requests, Allowed Origins |
| **27** | [AOP Advices (Masterclass)](documentation/Scenario27/README.md) | `@Around`, `@Before`, `@After` depth |
| **28** | [Rest Client (WebClient)](documentation/Scenario28/README.md) | Reactive vs RestTemplate |
| **29** | [Flyway Migrations](documentation/Scenario29/README.md) | Version control for Databases |
| **30** | [Secondary DB Config](documentation/Scenario30/README.md) | Multiple DataSources |
| **31** | [Scheduling Thread Pool](documentation/Scenario31/README.md) | `TaskScheduler` configuration |
| **32** | [Actuator Customization](documentation/Scenario32/README.md) | Custom `HealthIndicator` |
| **33** | [Task Scheduling (@Scheduled)](documentation/Scenario33/README.md) | `fixedRate` vs `fixedDelay` |
| **34** | [Async Return Types](documentation/Scenario34/README.md) | `CompletableFuture`, Parallel Orchestration |
| **35** | [Filter vs Interceptor](documentation/Scenario35/README.md) | Chain of responsibility, Context access |
| **36** | [Jackson Customization](documentation/Scenario36/README.md) | `@JsonView`, Custom Serializers |
| **38** | [API Versioning Strategies](documentation/Scenario38/README.md) | URI vs Header vs Media Type |
| **42** | [Spring Batch Basics](documentation/Scenario42/README.md) | Job, Step, ItemReader |
| **43** | [Websockets/STOMP](documentation/Scenario43/README.md) | Real-time communication |
| **44** | [Proxying & Final Class](documentation/Scenario44/README.md) | CGLIB vs JDK Dynamic Proxy |
| **45** | [Circuit Breaker Basics](documentation/Scenario45/README.md) | Resilience4j intro |
| **47** | [Method Security](documentation/Scenario47/README.md) | `@PreAuthorize`, `@PostAuthorize` |
| **48** | [OAuth2 Client Flow](documentation/Scenario48/README.md) | Social Login integration |
| **49** | [Resource Server](documentation/Scenario49/README.md) | Serving protected resources |
| **51** | [SSL/TLS (HTTPS)](documentation/Scenario51/README.md) | Keystores, Truststores |
| **52** | [CSRF Protection](documentation/Scenario52/README.md) | Tokens, Cookies, SameSite |
| **53** | [Authentication Flow](documentation/Scenario53/README.md) | ProviderManager, AuthProvider |
| **54** | [UserDetailsService](documentation/Scenario54/README.md) | Custom User loading logic |
| **55** | [Password Upgrading](documentation/Scenario55/README.md) | `DelegatingPasswordEncoder` |
| **56** | [Securing Actuators](documentation/Scenario56/README.md) | Restricted management endpoints |
| **57** | [CSP (Content Security Policy)](documentation/Scenario57/README.md) | XSS Prevention using Nonce |
| **58** | [URL Order & Matchers](documentation/Scenario58/README.md) | First-match-wins rule |
| **59** | [Roles vs Authorities](documentation/Scenario59/README.md) | `ROLE_` prefix convention |
| **60** | [Role Hierarchy](documentation/Scenario60/README.md) | Permission inheritance |
| **61** | [Auth Exception Handling](documentation/Scenario61/README.md) | 401 Unauth vs 403 Forbidden |
| **62** | [Ignoring vs Permitting](documentation/Scenario62/README.md) | Performance vs Security (The Filter Bypass) |
| **63** | [Session Management](documentation/Scenario63/README.md) | Stateless, Fixation, Concurrency |
| **64** | [Command Injection](documentation/Scenario64/README.md) | Safe OS interaction |
| **65** | [SQL Injection](debug-challenge/documentation/Scenario65/README.md) | Parameterized Queries vs Fragments |
| **66** | [Retry Patterns](debug-challenge/documentation/Scenario66/README.md) | `@Retryable`, Exponential Backoff, Jitter |
| **67** | [Saga Pattern](debug-challenge/documentation/Scenario67/README.md) | Distributed Transactions, Orchestration |
| **68** | [File Upload Security](debug-challenge/documentation/Scenario68/README.md) | Path Traversal, RCE, Content Validation |
| **69** | [DDoS Protection](debug-challenge/documentation/Scenario69/README.md) | Bucket4j, Rate Limiting, Throttling |
| **70** | [API Documentation](debug-challenge/documentation/Scenario70/README.md) | Swagger UI, OpenAPI 3, Annotations |
| **71** | [Clickjacking Protection](debug-challenge/documentation/Scenario71/README.md) | X-Frame-Options, CSP frame-ancestors |
| **72** | [Insecure Deserialization](debug-challenge/documentation/Scenario72/README.md) | Look-ahead validation, Allowlisting, RCE |
| **73** | [Timeout Chain Coherence](debug-challenge/documentation/Scenario73/README.md) | Resilience4j, Timeouts, Bulkheads |
| **74** | [SSRF Protection](debug-challenge/documentation/Scenario74/README.md) | URL Validation, Internal IP Blocking |
| **75** | [Custom Validations](debug-challenge/documentation/Scenario75/README.md) | Cross-field logic, DB-driven checks, i18n |
| **76** | [@Value vs @ConfigProps](debug-challenge/documentation/Scenario76/README.md) | Type-safety, Relaxed binding, Validation |
| **77** | [Actuator & Monitoring](debug-challenge/documentation/Scenario77/README.md) | Metrics, Custom Endpoints, Security |
| **78** | [Profiles & Config](debug-challenge/documentation/Scenario78/README.md) | Env-specific files, @Profile beans |
| **79** | [Advanced Logging](debug-challenge/documentation/Scenario79/README.md) | Logback-spring.xml, Rotation, Colors |
| **80** | [Custom JPA Queries](debug-challenge/documentation/Scenario80/README.md) | Derived Methods, JPQL, Native SQL |
| **81** | [JPA Auditing](debug-challenge/documentation/Scenario81/README.md) | @CreatedDate, @CreatedBy, AuditorAware |
| **82** | [Web Scopes](debug-challenge/documentation/Scenario82/README.md) | Request, Session, Scoped Proxies |
| **83** | [ABAC Security](debug-challenge/documentation/Scenario83/README.md) | SpEL, PermissionEvaluator |
| **84** | [MVC Interceptors](debug-challenge/documentation/Scenario84/README.md) | preHandle, postHandle, afterCompletion |
| **85** | [Filter Types](debug-challenge/documentation/Scenario85/README.md) | Standard vs OncePerRequest, Registration |
| **86** | [URL Data Extraction](debug-challenge/documentation/Scenario86/README.md) | @RequestParam vs @PathVariable, Regex Validation |
| **87** | [JPA One-to-One](debug-challenge/documentation/Scenario87/README.md) | Bidirectional, Cascade.ALL, Lazy Loading |
| **88** | [One-to-Many Masterclass](debug-challenge/documentation/Scenario88/README.md) | Hierarchical Mappings, Set vs List, Sync Helpers |
| **89** | [Many-to-Many & Intersection Entities](debug-challenge/documentation/Scenario89/README.md) | Bridge Tables, Relationship Attributes |
| **90** | [Bulkhead Pattern](documentation/Scenario90/README.md) | Semaphore vs Fixed Thread Pool, Isolation |
| **91** | [Multi-Tenant Architecture](documentation/Scenario91/README.md) | Database-per-tenant, Schema-per-tenant |
| **92** | [Read/Write Splitting](documentation/Scenario92/README.md) | Master/Slave, `AbstractRoutingDataSource` |
| **93** | [Transactional Essentials](documentation/Scenario93/README.md) | Propagation, Proxy Pitfalls, Rollback Rules |
| **94** | [ID GenerationTypes](documentation/Scenario94/README.md) | IDENTITY vs SEQUENCE vs UUID, Batching |
| **95** | [DTO Projections](documentation/Scenario95/README.md) | Interface vs Class Projections, Open/Closed |
| **96** | [Startup Env Validation](documentation/Scenario96/README.md) | Custom @ValidateEnv, Fail-Fast Architecture |
| **97** | [JPA Embeddables](documentation/Scenario97/README.md) | @Embeddable, @Embedded, @AttributeOverrides |
| **98** | [JPA Inheritance](documentation/Scenario98/README.md) | Single Table, @DiscriminatorColumn, Polymorphism |
| **99** | [JPA Composite Keys](documentation/Scenario99/README.md) | @EmbeddedId, @FieldDefaults, Serializable |
| **100** | [JSON Column Mapping](documentation/Scenario100/README.md) | Hibernate 6, @JdbcTypeCode, SqlTypes.JSON |
| **101** | [Declarative HTTP Clients](documentation/Scenario101/README.md) | Spring 6, @HttpExchange, RestClient |
| **102** | [Spring AI: First Handshake](documentation/Scenario102/README.md) | ChatClient, Gemini Integration, Streaming |
| **103** | [Structured Outputs (DTOs)](documentation/Scenario103/README.md) | BeanOutputParser, Type-safe AI responses |
| **104** | [Prompt Templates & Roles](documentation/Scenario104/README.md) | .st files, SystemMessage, Personas |
| **105** | [Conversational Memory](documentation/Scenario105/README.md) | ChatMemory, Session-based history |
| **106** | [RAG: Knowledge Retrieval](documentation/Scenario106/README.md) | Vector Stores, Embeddings, Document ETL |
| **107** | [AI Function Calling](documentation/Scenario107/README.md) | Tools, @Description, java.util.Function |
| **108** | [AI Observability](documentation/Scenario108/README.md) | Metrics, Token Tracking, Actuator, Redaction |
| **110** | [Jackson Properties](documentation/Scenario110/README.md) | @JsonProperty, @JsonIgnore, @JsonInclude, @JsonFormat |
| **111** | [External API Clients](documentation/Scenario111/README.md) | RestTemplate, WebClient, RestClient, Feign, @HttpExchange |
| **112** | [Asynchronous Masterclass](documentation/Scenario112/README.md) | @Async, CompletableFuture, DeferredResult, Async Events |
| **113** | [Spring Data REST](documentation/Scenario113/README.md) | HATEOAS, HAL, Projections, @RepositoryRestResource |
| **114** | [Remember Me Auth](documentation/Scenario114/README.md) | Persistent Token Repository, Series/Token theft detection |
| **115** | [@ModelAttribute](documentation/Scenario115/README.md) | Form binding, Model population, Pre-population methods |
| **116** | [@Order & Ordered](documentation/Scenario116/README.md) | Bean injection order, Filter chain precedence, Priority rules |
| **117** | [Jackson Polymorphism](documentation/Scenario117/README.md) | Inheritance in JSON, @JsonTypeInfo, @JsonSubTypes |
| **118** | [Unit vs Integration Testing](documentation/Scenario118/README.md) | @SpringBootTest vs @WebMvcTest, Testing Layers |
| **119** | [MockMvc Deep Dive](documentation/Scenario119/README.md) | Controller Testing, JsonPath, Headers, Params |
| **120** | [Service Layer Testing](documentation/Scenario120/README.md) | Mockito, BDD Style, ArgumentCaptor, Interactions |
| **121** | [Testcontainers (Database Integration Testing)](documentation/Scenario121/README.md) | Testcontainers, PostgreSQL, Integration Tests |
| **122** | [HikariCP Connection Pool Tuning](documentation/Scenario122/README.md) | HikariCP, Max Pool Size, Idle Timeout, Monitoring |
| **123** | [Thread Dump Analysis (Debugging Deadlocks)](documentation/Scenario123/README.md) | Thread Dumps, Deadlocks, jstack, VisualVM |
| **124** | [Memory Leak Detection in Spring Boot](documentation/Scenario124/README.md) | Heap Dumps, MAT, Leak Suspects, Static references |
| **125** | [Lazy vs Eager Loading (The N+1 Problem)](documentation/Scenario125/README.md) | @OneToMany, FetchType, JOIN FETCH, EntityGraph |
| **126** | [Cache Stampede Problem & Prevention](documentation/Scenario126/README.md) | @Cacheable(sync=true), Dog-piling, Database Load |
| **127** | [Caching Strategies (Aside, Through, Behind)](documentation/Scenario127/README.md) | Cache Aside, Write Through, Write Behind, @Async |
| **128** | [In-Memory Caching with Caffeine](documentation/Scenario128/README.md) | Caffeine, TTL, Size-based Eviction, Cache Stats |
| **129** | [Transaction Isolation Levels](documentation/Scenario129/README.md) | Dirty/Phantom Reads, @Transactional, MVCC, Isolation Levels |
| **130** | [Code Quality (PMD & Checkstyle)](documentation/Scenario130/README.md) | Static Analysis, PMD, Checkstyle, CI/CD Standards |
| **131** | [Architecture Enforcement (ArchUnit)](documentation/Scenario131/README.md) | Architectural Linting, Layer Isolation, Design Rules |
| **132** | [Mutation Testing (PITest)](documentation/Scenario132/README.md) | PITest, Killed vs Survived Mutants, Test Quality |
| **133** | [Spring Cloud Contract (CDCT)](documentation/Scenario133/README.md) | Consumer Driven Contract Testing, Stubs, Producer/Consumer flow |
| **134** | [Environmental Integrity (Maven Enforcer)](documentation/Scenario134/README.md) | Java/Maven Version Locking, Banned Dependencies, Convergence |
| **135** | [Microbenchmarking (JMH)](documentation/Scenario135/README.md) | Accurate Performance Testing, JVM Warmup, Dead Code Elimination |
| **136** | [Database Indexing](documentation/Scenario136/README.md) | B-Tree Indexes, Query Optimization, Full Table Scan vs Index Scan |

---

## 💎 Advanced Production Quality Stack
Beyond standard development, we've implemented a suite of advanced enforcement tools to mirror a high-maturity production environment. Click on any **Feature** to see the detailed guide.

| Feature | Scenario | Documentation | Key Focus |
| :--- | :--- | :--- | :--- |
| [**Static Analysis**](documentation/Scenario130/README.md) | 130 | [PMD & Checkstyle](documentation/Scenario130/README.md) | Code Style Consistency |
| [**Arch Integrity**](documentation/Scenario131/README.md) | 131 | [ArchUnit](documentation/Scenario131/README.md) | Circular Deps & Layer Isolation |
| [**Mutation Testing**](documentation/Scenario132/README.md) | 132 | [PITest](documentation/Scenario132/README.md) | Test Suite Effectiveness |
| [**Contract Testing**](documentation/Scenario133/README.md) | 133 | [Spring Cloud Contract](documentation/Scenario133/README.md) | Microservice Integration |
| [**Environments**](documentation/Scenario134/README.md) | 134 | [Maven Enforcer](documentation/Scenario134/README.md) | Java/Maven Version Locking |
| [**Benchmark**](documentation/Scenario135/README.md) | 135 | [JMH](documentation/Scenario135/README.md) | High-Precision Performance |

---

## 🛠️ Best Practices & Architecture
Beyond coding scenarios, we also maintain documentation on high-level Spring Boot design:

- 🚀 **[Spring Boot Anti-Patterns](documentation/AntiPatterns/README.md)**: Common pitfalls and how to avoid them (Field Injection, God Services, Proxy issues, N+1, etc.).

---

## 🏗️ How to Run
1.  **Clone** the repository.
2.  **Start the app**: `mvn spring-boot:run`
3.  **Explore**: Each scenario has its own endpoint at `/api/scenarioXX/...`.
4.  **Verify**: Check the specific `README.md` for each scenario to see `curl` commands and expected results.
