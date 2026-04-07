# Spring Boot Interview Preparation Hub: The Debug Challenge

This repository is a comprehensive guide to Spring Boot and Spring Security interview scenarios. Each scenario is implemented as a "Debug Challenge" to help you understand core concepts, common pitfalls, and architectural patterns.

---

## 🚀 Scenario Index

| Scenario | Topic | Key Concepts |
| :--- | :--- | :--- |
| **01** | [Bean Lifecycle](file:///documentation/Scenario1/README.md) | `@PostConstruct`, `@PreDestroy`, Bean Initialization |
| **02** | [Bean Scopes Masterclass](file:///documentation/Scenario2/README.md) | Singleton vs Prototype, Injection Pitfalls |
| **03** | [Injection Patterns](file:///documentation/Scenario3/README.md) | Constructor vs Field vs Setter, Best Practices |
| **04** | [Qualifiers & Primary](file:///documentation/Scenario4/README.md) | Disambiguation, Handling Multiple Beans |
| **05** | [Circular Dependencies](file:///documentation/Scenario5/README.md) | Injection patterns, `@Lazy` resolution |
| **06** | [Concurrency Control](file:///documentation/Scenario6/README.md) | Race Conditions, Pessimistic vs Optimistic Locking |
| **07** | [Async Processing](file:///documentation/Scenario7/README.md) | `@Async`, SecurityContext propagation |
| **08** | [JWT Auth](file:///documentation/Scenario8/README.md) | Token generation, validation, blacklisting |
| **09** | [JPA N+1 Problem](file:///documentation/Scenario9/README.md) | `JOIN FETCH`, Entity Graphs |
| **10** | [Validation logic](file:///documentation/Scenario10/README.md) | Custom `@Constraint`, Hibernate Validator |
| **11** | [Global Exception Handling](file:///documentation/Scenario11/README.md) | `@ControllerAdvice`, `ProblemDetail` |
| **12** | [Spring Events](file:///documentation/Scenario12/README.md) | `@EventListener`, Async Events |
| **13** | [Caching (Redis)](file:///documentation/Scenario13/README.md) | `@Cacheable`, TTL, Cache Eviction |
| **14** | [Idempotency](file:///documentation/Scenario14/README.md) | Idempotent keys, Distributed Locks |
| **15** | [Rate Limiting](file:///documentation/Scenario15/README.md) | Bucket4j, API Throtelling |
| **16** | [Profiles & Config](file:///documentation/Scenario16/README.md) | `@Profile`, Conditional Beans |
| **17** | [AOP Introduction](file:///documentation/Scenario17/README.md) | Aspect, Pointcuts, Advices |
| **18** | [Transaction Propagation](file:///documentation/Scenario18/README.md) | `REQUIRED`, `REQUIRES_NEW`, Rollback rules |
| **20** | [Custom @PropertySource](file:///documentation/Scenario20/README.md) | Externalized Config, Environment Abstraction |
| **22** | [Custom Annotations](file:///documentation/Scenario22/README.md) | Creating custom AOP-driven decorators |
| **23** | [Pagination & Sorting](file:///documentation/Scenario23/README.md) | `Pageable`, `Sort`, HATEOAS basics |
| **24** | [Content Negotiation](file:///documentation/Scenario24/README.md) | JSON vs XML, Accept headers |
| **25** | [Observability](file:///documentation/Scenario25/README.md) | Micrometer, Tracing, Spans |
| **26** | [CORS Deep Dive](file:///documentation/Scenario26/README.md) | Preflight requests, Allowed Origins |
| **27** | [AOP Advices (Masterclass)](file:///documentation/Scenario27/README.md) | `@Around`, `@Before`, `@After` depth |
| **28** | [Rest Client (WebClient)](file:///documentation/Scenario28/README.md) | Reactive vs RestTemplate |
| **29** | [Flyway Migrations](file:///documentation/Scenario29/README.md) | Version control for Databases |
| **30** | [Secondary DB Config](file:///documentation/Scenario30/README.md) | Multiple DataSources |
| **31** | [Scheduling Thread Pool](file:///documentation/Scenario31/README.md) | `TaskScheduler` configuration |
| **32** | [Actuator Customization](file:///documentation/Scenario32/README.md) | Custom `HealthIndicator` |
| **33** | [Task Scheduling (@Scheduled)](file:///documentation/Scenario33/README.md) | `fixedRate` vs `fixedDelay` |
| **34** | [Async Return Types](file:///documentation/Scenario34/README.md) | `CompletableFuture`, Parallel Orchestration |
| **35** | [Filter vs Interceptor](file:///documentation/Scenario35/README.md) | Chain of responsibility, Context access |
| **36** | [Jackson Customization](file:///documentation/Scenario36/README.md) | `@JsonView`, Custom Serializers |
| **38** | [API Versioning Strategies](file:///documentation/Scenario38/README.md) | URI vs Header vs Media Type |
| **42** | [Spring Batch Basics](file:///documentation/Scenario42/README.md) | Job, Step, ItemReader |
| **43** | [Websockets/STOMP](file:///documentation/Scenario43/README.md) | Real-time communication |
| **44** | [Proxying & Final Class](file:///documentation/Scenario44/README.md) | CGLIB vs JDK Dynamic Proxy |
| **45** | [Circuit Breaker Basics](file:///documentation/Scenario45/README.md) | Resilience4j intro |
| **47** | [Method Security](file:///documentation/Scenario47/README.md) | `@PreAuthorize`, `@PostAuthorize` |
| **48** | [OAuth2 Client Flow](file:///documentation/Scenario48/README.md) | Social Login integration |
| **49** | [Resource Server](file:///documentation/Scenario49/README.md) | Serving protected resources |
| **51** | [SSL/TLS (HTTPS)](file:///documentation/Scenario51/README.md) | Keystores, Truststores |
| **52** | [CSRF Protection](file:///documentation/Scenario52/README.md) | Tokens, Cookies, SameSite |
| **53** | [Authentication Flow](file:///documentation/Scenario53/README.md) | ProviderManager, AuthProvider |
| **54** | [UserDetailsService](file:///documentation/Scenario54/README.md) | Custom User loading logic |
| **55** | [Password Upgrading](file:///documentation/Scenario55/README.md) | `DelegatingPasswordEncoder` |
| **56** | [Securing Actuators](file:///documentation/Scenario56/README.md) | Restricted management endpoints |
| **57** | [CSP (Content Security Policy)](file:///documentation/Scenario57/README.md) | XSS Prevention using Nonce |
| **58** | [URL Order & Matchers](file:///documentation/Scenario58/README.md) | First-match-wins rule |
| **59** | [Roles vs Authorities](file:///documentation/Scenario59/README.md) | `ROLE_` prefix convention |
| **60** | [Role Hierarchy](file:///documentation/Scenario60/README.md) | Permission inheritance |
| **61** | [Auth Exception Handling](file:///documentation/Scenario61/README.md) | 401 Unauth vs 403 Forbidden |
| **62** | [Ignoring vs Permitting](file:///documentation/Scenario62/README.md) | Performance vs Security (The Filter Bypass) |
| **63** | [Session Management](file:///documentation/Scenario63/README.md) | Stateless, Fixation, Concurrency |
| **64** | [Command Injection](file:///documentation/Scenario64/README.md) | Safe OS interaction |
| **65** | [SQL Injection](file:///debug-challenge/documentation/Scenario65/README.md) | Parameterized Queries vs Fragments |
| **66** | [Retry Patterns](file:///debug-challenge/documentation/Scenario66/README.md) | `@Retryable`, Exponential Backoff, Jitter |
| **67** | [Saga Pattern](file:///debug-challenge/documentation/Scenario67/README.md) | Distributed Transactions, Orchestration |
| **68** | [File Upload Security](file:///debug-challenge/documentation/Scenario68/README.md) | Path Traversal, RCE, Content Validation |
| **69** | [DDoS Protection](file:///debug-challenge/documentation/Scenario69/README.md) | Bucket4j, Rate Limiting, Throttling |
| **70** | [API Documentation](file:///debug-challenge/documentation/Scenario70/README.md) | Swagger UI, OpenAPI 3, Annotations |
| **71** | [Clickjacking Protection](file:///debug-challenge/documentation/Scenario71/README.md) | X-Frame-Options, CSP frame-ancestors |
| **72** | [Insecure Deserialization](file:///debug-challenge/documentation/Scenario72/README.md) | Look-ahead validation, Allowlisting, RCE |
| **73** | [Timeout Chain Coherence](file:///debug-challenge/documentation/Scenario73/README.md) | Resilience4j, Timeouts, Bulkheads |
| **74** | [SSRF Protection](file:///debug-challenge/documentation/Scenario74/README.md) | URL Validation, Internal IP Blocking |
| **75** | [Custom Validations](file:///debug-challenge/documentation/Scenario75/README.md) | Cross-field logic, DB-driven checks, i18n |
| **76** | [@Value vs @ConfigProps](file:///debug-challenge/documentation/Scenario76/README.md) | Type-safety, Relaxed binding, Validation |
| **77** | [Actuator & Monitoring](file:///debug-challenge/documentation/Scenario77/README.md) | Metrics, Custom Endpoints, Security |
| **78** | [Profiles & Config](file:///debug-challenge/documentation/Scenario78/README.md) | Env-specific files, @Profile beans |
| **79** | [Advanced Logging](file:///debug-challenge/documentation/Scenario79/README.md) | Logback-spring.xml, Rotation, Colors |
| **80** | [Custom JPA Queries](file:///debug-challenge/documentation/Scenario80/README.md) | Derived Methods, JPQL, Native SQL |
| **81** | [JPA Auditing](file:///debug-challenge/documentation/Scenario81/README.md) | @CreatedDate, @CreatedBy, AuditorAware |
| **82** | [Web Scopes](file:///debug-challenge/documentation/Scenario82/README.md) | Request, Session, Scoped Proxies |
| **83** | [ABAC Security](file:///debug-challenge/documentation/Scenario83/README.md) | SpEL, PermissionEvaluator |
| **84** | [MVC Interceptors](file:///debug-challenge/documentation/Scenario84/README.md) | preHandle, postHandle, afterCompletion |
| **85** | [Filter Types](file:///debug-challenge/documentation/Scenario85/README.md) | Standard vs OncePerRequest, Registration |
| **86** | [URL Data Extraction](file:///debug-challenge/documentation/Scenario86/README.md) | @RequestParam vs @PathVariable, Regex Validation |
| **87** | [JPA One-to-One](file:///debug-challenge/documentation/Scenario87/README.md) | Bidirectional, Cascade.ALL, Lazy Loading |
| **88** | [One-to-Many Masterclass](file:///debug-challenge/documentation/Scenario88/README.md) | Hierarchical Mappings, Set vs List, Sync Helpers |
| **89** | [Many-to-Many & Intersection Entities](file:///debug-challenge/documentation/Scenario89/README.md) | Bridge Tables, Relationship Attributes |
| **90** | [Bulkhead Pattern](file:///documentation/Scenario90/README.md) | Semaphore vs Fixed Thread Pool, Isolation |
| **91** | [Multi-Tenant Architecture](file:///documentation/Scenario91/README.md) | Database-per-tenant, Schema-per-tenant |
| **92** | [Read/Write Splitting](file:///documentation/Scenario92/README.md) | Master/Slave, `AbstractRoutingDataSource` |
| **93** | [Transactional Essentials](file:///documentation/Scenario93/README.md) | Propagation, Proxy Pitfalls, Rollback Rules |
| **94** | [ID GenerationTypes](file:///documentation/Scenario94/README.md) | IDENTITY vs SEQUENCE vs UUID, Batching |
| **95** | [DTO Projections](file:///documentation/Scenario95/README.md) | Interface vs Class Projections, Open/Closed |
| **96** | [Startup Env Validation](file:///documentation/Scenario96/README.md) | Custom @ValidateEnv, Fail-Fast Architecture |
| **97** | [JPA Embeddables](file:///documentation/Scenario97/README.md) | @Embeddable, @Embedded, @AttributeOverrides |
| **98** | [JPA Inheritance](file:///documentation/Scenario98/README.md) | Single Table, @DiscriminatorColumn, Polymorphism |
| **99** | [JPA Composite Keys](file:///documentation/Scenario99/README.md) | @EmbeddedId, @FieldDefaults, Serializable |
| **100** | [JSON Column Mapping](file:///documentation/Scenario100/README.md) | Hibernate 6, @JdbcTypeCode, SqlTypes.JSON |
| **101** | [Declarative HTTP Clients](file:///documentation/Scenario101/README.md) | Spring 6, @HttpExchange, RestClient |
| **102** | [Spring AI: First Handshake](file:///documentation/Scenario102/README.md) | ChatClient, Gemini Integration, Streaming |
| **103** | [Structured Outputs (DTOs)](file:///documentation/Scenario103/README.md) | BeanOutputParser, Type-safe AI responses |
| **104** | [Prompt Templates & Roles](file:///documentation/Scenario104/README.md) | .st files, SystemMessage, Personas |
| **105** | [Conversational Memory](file:///documentation/Scenario105/README.md) | ChatMemory, Session-based history |
| **106** | [RAG: Knowledge Retrieval](file:///documentation/Scenario106/README.md) | Vector Stores, Embeddings, Document ETL |
| **107** | [AI Function Calling](file:///documentation/Scenario107/README.md) | Tools, @Description, java.util.Function |
| **108** | [AI Observability](file:///documentation/Scenario108/README.md) | Metrics, Token Tracking, Actuator, Redaction |
| **110** | [Jackson Properties](file:///documentation/Scenario110/README.md) | @JsonProperty, @JsonIgnore, @JsonInclude, @JsonFormat |
| **111** | [External API Clients](file:///documentation/Scenario111/README.md) | RestTemplate, WebClient, RestClient, Feign, @HttpExchange |
| **112** | [Asynchronous Masterclass](file:///documentation/Scenario112/README.md) | @Async, CompletableFuture, DeferredResult, Async Events |

---

## 🛠️ Best Practices & Architecture
Beyond coding scenarios, we also maintain documentation on high-level Spring Boot design:

- 🚀 **[Spring Boot Anti-Patterns](file:///documentation/AntiPatterns/README.md)**: Common pitfalls and how to avoid them (Field Injection, God Services, Proxy issues, N+1, etc.).

---

## 🏗️ How to Run
1.  **Clone** the repository.
2.  **Start the app**: `mvn spring-boot:run`
3.  **Explore**: Each scenario has its own endpoint at `/api/scenarioXX/...`.
4.  **Verify**: Check the specific `README.md` for each scenario to see `curl` commands and expected results.
