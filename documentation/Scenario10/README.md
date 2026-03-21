# Scenario 10: Global Exception Handling & ResponseStatusException

Demonstrates clean ways to map Java exceptions to meaningful HTTP status codes.

## Concept
Throwing raw `RuntimeException` results in a ugly `500 Internal Server Error`. In a professional API, you want:
- 400 for bad input (`IllegalArgumentException`).
- 404 for missing data (`ResourceNotFoundException`).
- 403 for unauthorized access.

## Implementation Details
We showed a `Scenario10Controller` that triggers different error scenarios matched to specific HTTP codes.

### ResponseStatusException Snippet:
```java
@GetMapping("/test/response-status")
public String triggerResponseStatus() {
    throw new org.springframework.web.server.ResponseStatusException(
        HttpStatus.FORBIDDEN, "Access Denied by ResponseStatusException!");
}
```

## Verification Results
- **Bad Request**: `/api/scenario10/test/bad-request` -> Returns `400 Bad Request`.
- **Not Found**: `/api/scenario10/test/not-found` -> Returns `404 Not Found`.

## Interview Theory: Exception Handling
- **@RestControllerAdvice**: The "Industry Standard." A global class that catches exceptions from any controller and converts them to JSON error bodies.
- **ResponseStatusException**: Introduced in Spring 5. It's lightweight and doesn't require custom exception classes or `@ControllerAdvice`.
- **The Why**: Clear error codes help frontend developers handle errors correctly (e.g., showing a login popup for 401 vs a generic "Something went wrong" for 500).
