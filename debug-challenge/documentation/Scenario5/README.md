# Scenario 5: Performance Monitoring & Report Generation

Demonstrates simple performance tracking and service-level logic.

## Concept
In production apps, tracking how long a service takes to execute is vital for identifying bottlenecks. This scenario shows a basic manual implementation of timing a service call.

## Implementation Details
The `Scenario5Controller` calls `ReportService` and calculates the time taken to generate a user-specific report.

### Controller Snippet:
```java
@GetMapping("/report/{userId}")
public String getReport(@PathVariable String userId) {
    long startTime = System.currentTimeMillis();
    String report = reportService.generateUserReport(userId);
    long duration = System.currentTimeMillis() - startTime;
    return "Report: " + report + "\nTime taken: " + duration + "ms";
}
```

## Verification Results
- **URL**: `http://localhost:8080/api/scenario5/report/USER123`
- **Result**: 
  ```text
  Report: User Report for 123
  Time taken: 100ms
  ```
- **Observation**: The response includes both the data and the execution time, simulating a performance log.

## Interview Theory: Performance Tracking
- **StopWatch**: While `System.currentTimeMillis()` works, Spring provides the `StopWatch` utility class for cleaner timing of multiple tasks.
- **AOP (Aspect Oriented Programming)**: For a professional approach, use AOP to intercept service calls and log their timing automatically without cluttering the business logic.
- **Micrometer/Prometheus**: In real microservices, use a metrics library to push these timings to a dashboard like Grafana.
