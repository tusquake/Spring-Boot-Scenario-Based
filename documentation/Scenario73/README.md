# Scenario 73: Timeout Chain Coherence

## Overview
In a microservices architecture, requests often flow through multiple services (Service A -> Service B -> Service C). **Timeout Chain Coherence** is the principle of ensuring that timeout values are synchronized across this chain.

If timeouts are not coherent, you can end up with "Zombie Requests"—downstream services still processing a request that the upstream service has already given up on.

## The Golden Rule 🌟
**Downstream Timeout < Upstream Timeout**

- If Service A (Upstream) has a timeout of **2s**.
- Service B (Downstream) MUST have a timeout **less than 2s** (e.g., **1s**).
- This ensures Service B fails *before* Service A, allowing Service A to receive the error and handle it (or return a fallback) before it times out itself.

## Implementation with Resilience4j
We use `@TimeLimiter` to enforce these boundaries. In our `application.properties`:

```properties
# Service A (Upstream) expects a result in 2s
resilience4j.timelimiter.instances.serviceA.timeout-duration=2s

# Service B (Downstream) must finish in 1s
resilience4j.timelimiter.instances.serviceB.timeout-duration=1s
```

## Why propagation matters?
Even better than fixed timeouts is **Timeout Propagation**. The upstream service sends the "remaining time" in a header (e.g., `X-Request-Timeout`), and the downstream service adjusts its timeout dynamically.

## Interview Tips 💡
- **What happens if Downstream > Upstream?** The client (User) sees a timeout from Service A, but Service B continues to consume CPU/Threads/Memory for a request that no one is listening to anymore. This leads to **Resource Exhaustion**.
- **How to calculate coherence?** Remember to account for network latency and overhead. If A calls B, then `Timeout(B) = Timeout(A) - NetworkLatency - Buffer`.
- **What is a "Stray" or "Zombie" request?** A request that is still being processed by a downstream service after the caller has timed out.

## Testing the Scenario
1. **Coherent Success**: `GET /api/scenario73/service-a?delayB=500` (500ms < 1s < 2s). Should succeed.
2. **Downstream Timeout**: `GET /api/scenario73/service-a?delayB=1500` (1500ms > 1s). Service B will timeout first, Service A will catch it and return a fallback.
3. **Total Exhaustion**: If Service A's timeout were shorter than B's, A would fail while B is still working.
