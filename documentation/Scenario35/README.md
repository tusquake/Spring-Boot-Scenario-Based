# Scenario 35: Enterprise HTTP Headers

Demonstrates how to handle advanced HTTP metadata for Traceability, Security, and Proxy handling.

## Concept
In a modern microservice architecture, a request passes through several layers (Browser -> Gateway -> Load Balancer -> Service). Standard headers help track and secure this flow.

## Implementation Details
1. **X-Correlation-ID**: An ID passed from the gateway to track a request across different services.
2. **X-RateLimit**: Sending back usage metadata so the client knows how many requests they have left.
3. **X-Forwarded-For**: How to find the user's real IP address when you are sitting behind a Proxy.

### Code Snippet:
```java
@RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor
```

## Verification Results
- **Trace**: `/api/scenario35/trace` -> Generates and returns a Correlation ID.
- **Limits**: `/api/scenario35/limited` -> Check headers; you'll see your "limit" and "remaining" quota.
- **Identity**: `/api/scenario35/whoami` -> Displays your User-Agent and real IP.

## Interview Theory: HTTP Protocols
- **Observability**: Correlation IDs are the backbone of distributed tracing. If Service A calls Service B, it must pass this ID along.
- **Trusting Headers**: Caution the interviewer: Never trust `X-Forwarded-For` without validating it, as an attacker can easily spoof it to bypass IP-based security.
- **Idempotency Headers**: Mention that custom headers are also used for Idempotency keys.
