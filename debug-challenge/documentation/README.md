# Scenario 1: Component Scan & Dependency Injection

Demonstrates how Spring Boot automatically finds and wires your beans.

## Concept
Spring Boot uses `@ComponentScan` (part of `@SpringBootApplication`) to scan for classes annotated with `@Component`, `@Service`, `@Repository`, or `@RestController`. If a bean is in a different package outside the scan range, it won't be found.

## Implementation Details
The `Scenario1Controller` depends on an `OrderService` which is located in a different package (`com.interview.external.service`).

### Controller Snippet:
```java
@RestController
@RequestMapping("/api/scenario1")
public class Scenario1Controller {
    private final OrderService orderService; // Injected via Constructor

    public Scenario1Controller(OrderService orderService) {
        this.orderService = orderService;
    }
}
```

## Verification Results
- **URL**: `http://localhost:8080/api/scenario1/test`
- **Result**: `Scenario 1 Success: [Order Data]`
- **Observation**: The `OrderService` was correctly injected even though it belongs to the `external` package, proving the component scan is working.

## Interview Theory: Dependency Injection
- **Constructor vs Field Injection**: Always prefer Constructor injection. It makes dependencies mandatory, allows for `final` fields, and simplifies unit testing.
- **Package Scanning**: By default, Spring scans the package of the class with `@SpringBootApplication` and all its sub-packages.
- **External Packages**: If you need to scan a package like `com.company.external`, you may need to add `@ComponentScan("com.company.external")` or define those beans manually in a `@Configuration` class.
