# Scenario 66: Retry Patterns & Exponential Backoff

In modern distributed systems, **Transient Failures** (short-lived network glips, DB locks) are common. Instead of failing immediately, we use **Retries**.

---

## 📈 Backoff Strategies

| Strategy | Pattern | Impact |
| :--- | :--- | :--- |
| **Fixed** | 1s, 1s, 1s | Simple, but can overwhelm a struggling service. |
| **Exponential** | 1s, 2s, 4s, 8s | Gives the downstream service more time to recover. |
| **Exponential + Jitter** | 1.1s, 2.4s, 3.8s | **Best Practice**. Prevents "Thundering Herd" (multiple clients retrying at the exact same time). |

---

## 🛠️ Implementation Details
This scenario uses **Spring Retry** with the `@Retryable` annotation. 

### Why `@Recover`?
If all attempts fail, the method annotated with `@Recover` is triggered. This is where you implement your **Fallback Logic** (Return cached data, send to a queue, or alert an engineer).

### Key Features used in [FlickeringService](file:///c:\Users\tushar.seth\Desktop\Scenario%20Based\debug-challenge\src\main\java\com\interview\debug\service\FlickeringService.java):
- `maxAttempts = 4`: Limit the duration of retries.
- `multiplier = 2.0`: Enables exponential growth of wait time.
- `random = true`: Adds jitter to stagger retries across different clients.

---

## 🚀 How to Test
1. **Trigger Retry**:
   `curl "http://localhost:8080/api/scenario66/retry"`
2. **Watch the Logs**: 
   - You will see 4 attempts in the console output.
   - Notice the **wait time increasing** between attempts.

---

## Interview Questions on Retries
- **Q: Should you retry for a 404 (Not Found) error?**
  - **A**: No. Only retry for **Internal Server Errors (5xx)** or timeouts. Retrying a 4xx error (client error) just wastes resources.
- **Q: What is Idempotency and why is it required for Retries?**
  - **A**: If you retry a "Charge Money" operation, you must ensure you don't charge the user twice! The operation must be **Idempotent**.
- **Q: What is a Thundering Herd problem?**
  - **A**: When a service goes down and many clients all retry at the exact same fixed interval, the massive surge of traffic can crash the service as soon as it tries to come back up. **Jitter** prevents this.
