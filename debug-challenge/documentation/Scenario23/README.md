# Scenario 23: Circuit Breaker & Fallbacks (Resilience4j)

Demonstrates how to handle downstream service failures gracefully using Circuit Breakers.

## Concept
If a downstream service is down, your app shouldn't hang or crash. A **Circuit Breaker** "trips" after a certain number of failures, immediately returning a **Fallback** response instead of trying to call the broken service.

## Implementation Details
We used **Resilience4j** with the `@CircuitBreaker` annotation.

### Code Snippet:
```java
@CircuitBreaker(name = "externalService", fallbackMethod = "fallback")
public String recoverableCall(...) {
    return downstreamService.callExternalApi(fail);
}

public String fallback(boolean fail, Exception e) {
    return "Fallback: Service is temporarily unavailable. Returning cached data.";
}
```

## Verification Results
- **Happy Path**: Call with `fail=false`. -> Returns normal response.
- **Trip Path**: Call with `fail=true` multiple times.
- **Result**: The circuit opens. Even if you call with `fail=false` now, it still returns the **Fallback** instantly without even trying to call the service.

## Interview Theory: Resilience
- **Circuit States**:
  - **Closed**: Normal (traffic flows).
  - **Open**: Tripped (traffic blocked, returns fallback).
  - **Half-Open**: Probing (allows a few requests to see if service is back).
- **Fallback Rule**: Fallback methods must have the same parameter list as the original method, plus an optional Exception parameter.
- **Side effects**: Circuit breakers prevent "Cascading Failures" where one dead service brings down the entire microservice ecosystem.
