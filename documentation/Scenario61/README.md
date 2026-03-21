# Scenario 61: Custom Exception Handling (401 vs 403)

Providing professional, structured JSON error responses for security failures instead of default HTML error pages.

## Concept
- **401 Unauthorized**: User is not authenticated (The "Who are you?" error). Handled by `AuthenticationEntryPoint`.
- **403 Forbidden**: User is authenticated but lacks permission (The "You can't do that" error). Handled by `AccessDeniedHandler`.

## Implementation Details
We created custom classes to capture these events and write a JSON body to the response stream.

### 401 Handler (EntryPoint):
```java
public void commence(...) {
    body.put("error", "Unauthorized");
    body.put("message", "Full authentication is required. Please provide a valid token.");
    // ... write to response ...
}
```

### 403 Handler:
```java
public void handle(...) {
    body.put("error", "Forbidden");
    body.put("message", "You do not have the necessary permissions (roles) for this action.");
    // ... write to response ...
}
```

## Verification Results
1. **Unauthorized Test**: Call `/api/scenario61/protected` with no headers.
   - **Result**: `401 Unauthorized` with custom JSON: `{"error":"Unauthorized", "message":"..."}`.
2. **Forbidden Test**: Call `/api/scenario61/admin-only` with a regular User token.
   - **Result**: `403 Forbidden` with custom JSON: `{"error":"Forbidden", "message":"..."}`.

## Interview Theory: EntryPoint vs Handler
- **Filter vs Controller**: Security exceptions happen in the **Filters** (Layer 1), while `@ControllerAdvice` lives in the **Controller** (Layer 3). Because the filters run "upstream," security errors never reach the Controller, making these specialized handlers necessary.
- **User Experience**: A pure JSON API should never return an HTML "Whitelabel Error Page." Using these handlers ensures consistency for mobile and web frontends.
