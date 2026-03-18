# Scenario 14: API Idempotency (AOP)

Demonstrates how to prevent duplicate processing of the same request using Aspects and a unique request Key.

## Concept
If a user clicks "Pay" twice due to lag, you don't want to charge them twice. **Idempotency** ensures that repeated identical requests produce the same result without side effects.

## Implementation Details
We used a custom `@Idempotent` annotation and an Aspect (`IdempotencyAspect`) to intercept requests.

### Usage:
```java
@PostMapping("/pay")
@Idempotent
public ResponseEntity<Map<String, Object>> processPayment(...) { ... }
```

## Verification Results
1. **First Request**: Send a request with header `X-Idempotency-Key: KEY001`.
   - **Result**: `SUCCESS` (Full processing happens).
2. **Duplicate Request**: Send the same header again within 5 minutes.
   - **Result**: `CACHED SUCCESS` (The aspect returns the old response immediately without calling the controller).

## Interview Theory: Idempotency
- **GET vs POST**: GET, PUT, and DELETE are naturally idempotent. POST is NOT idempotent by default.
- **The Mechanism**: You usually store the `RequestKey` + `ResponsePayload` in a fast cache like **Redis** with a short TTL (e.g., 24 hours).
- **HTTP 409**: If a request is still being processed while a duplicate arrives, it's often best to return `409 Conflict`.
