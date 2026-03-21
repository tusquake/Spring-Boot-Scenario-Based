# Scenario 22: API Rate Limiting

Demonstrates how to protect your API from abuse by limiting the number of requests a user can make in a given timeframe.

## Concept
Rate limiting is essential to prevent Denial of Service (DoS) attacks and ensure fair usage of resources. It restricts users to a maximum number of requests (e.g., 5 per minute).

## Implementation Details
The `Scenario22Controller` is a simple protected endpoint. The actual limiting logic is usually implemented in a **Servlet Filter** or an **API Gateway**.

### Controller Snippet:
```java
@GetMapping("/test-limit")
public String testLimit() {
    return "Success! You are within the rate limit (5 requests per minute).";
}
```

## Verification Results
- **Normal Usage**: Send 1-5 requests within a minute. -> Returns `200 OK`.
- **Abuse**: Send the 6th request within the same minute. -> Returns `429 Too Many Requests`.

## Interview Theory: Rate Limiting
- **Algorithms**: Common algorithms include **Token Bucket**, **Leaky Bucket**, and **Fixed Window Counter**.
- **Storage**: In a distributed environment, use **Redis** to store the request counts so that all instances of your microservice share the same limit.
- **HTTP 429**: This is the standard status code for rate limiting. Always include a `Retry-After` header to tell the client when they can try again.
- **Bucket4j**: A popular library for implementing rate limiting in Java/Spring Boot.
