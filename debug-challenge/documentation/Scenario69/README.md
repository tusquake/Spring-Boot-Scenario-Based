# Scenario 69: DDoS Protection & Rate Limiting

DDoS (Distributed Denial of Service) is an attempt to overwhelm your application with a flood of traffic. This scenario demonstrates **Application-Level Rate Limiting** using the **Token Bucket Algorithm**.

---

## 🛡️ Defensive Layers

| Layer | defense Strategy | Description |
| :--- | :--- | :--- |
| **Network (L3/L4)** | AWS Shield / Cloudflare | Stops massive volumetric floods before they reach your servers. |
| **Application (L7)** | WAF (Web App Firewall) | Inspects HTTP headers, prevents SQLi, and blocks bot-like patterns. |
| **App Level** | **Rate Limiting** | Throttles individual IPs (prevents a single rogue user from crashing the app). |

---

## 🛠️ implementation Details
In this app, we use **Bucket4j** with an in-memory `RateLimitingService`:

1. **Algorithm**: Token Bucket (10 tokens max, refills over time).
2. **Identifier**: `request.getRemoteAddr()` (The Client IP).
3. **HTTP Response**: If the bucket is empty, we return **429 Too Many Requests**.

### Why use Error 429?
It explicitly tells the client and downstream proxies (like Load Balancers) that the limit was reached, allowing them to back off gracefully.

---

## 🚀 How to Test
1. **Try Accessing**:
   `curl -i http://localhost:8080/api/scenario69/secure-data`
2. **Simulate Attack (Bash loop)**:
   `for i in {1..15}; do curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/api/scenario69/secure-data; done`
   - *Result*: You will see several `200` responses followed by `429` errors.

---

## Interview Questions on DDoS
- **Q: What is the difference between a DDoS and a Spike?**
  - **A**: A **Spike** is legitimate traffic (e.g., Marketing push). A **DDoS** is malicious. High-quality WAFs use heuristics to identify non-human behavior.
- **Q: Why don't we store rate limits in the database?**
  - **A**: Databases are slow. Storing limits in a DB would make the rate limiter itself a bottleneck. Use **Redis** for distributed rate limiting.
- **Q: What is the Token Bucket vs Leaky Bucket algorithm?**
  - **A**: **Token Bucket** allows bursts of traffic (up to the bucket size). **Leaky Bucket** forces a constant, steady flow.
