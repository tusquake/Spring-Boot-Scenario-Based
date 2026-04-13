# Scenario 122: HikariCP Connection Pool Tuning

Database connection creation is expensive. A connection pool like **HikariCP** (Spring Boot's default) maintains a reservoir of ready-to-use connections, significantly improving application performance.

## 🚀 Key Tuning Parameters

### 1. `maximum-pool-size` (Default: 10)
Indicates the maximum number of actual connections to the database.
- **Too small:** Requests will hang waiting for a connection (ConnectionTimeout).
- **Too large:** Wastage of database memory and potential "connection flood" during traffic spikes.
- **Rule of Thumb:** `connections = ((core_count * 2) + effective_spindle_count)`

### 2. `minimum-idle` (Default: same as max)
The minimum number of idle connections Hikari maintains. Keeping this the same as `maximum-pool-size` (fixed-size pool) is often recommended for maximum performance.

### 3. `connection-timeout` (Default: 30000ms)
The maximum time a client will wait for a connection from the pool. If this time is exceeded without a connection becoming available, a `SqlException` is thrown.

### 4. `idle-timeout` (Default: 600000ms)
The maximum amount of time that a connection is allowed to sit idle in the pool.

### 5. `max-lifetime` (Default: 1800000ms)
The maximum lifetime of a connection in the pool. Connections should be retired before the database's own connection timeout to avoid stale connection errors.

### 6. `leak-detection-threshold` (Default: 0 - disabled)
If a connection is out of the pool for longer than this threshold, HikariCP will log a warning message indicating a possible connection leak.

---

## 📊 Pool Performance Monitoring

By enabling Actuator and Hikari metrics, you can observe:
- **Active Connections:** Currently being used by threads.
- **Idle Connections:** Waiting in the pool.
- **Threads Awaiting Connection:** Number of requests currently blocked.

---

## 🛠️ Implementation Details

In this scenario:
1.  **Metric Exposure:** Enabled Spring Boot Actuator to expose HikariCP metrics.
2.  **Saturation Demo:** Endpoint `/scenario122/stress` simulates expensive operations to show pool exhaustion.
3.  **Leak Detection Demo:** Endpoint `/scenario122/leak` purposefully holds a connection without closing it to trigger a warning in the logs after 2 seconds.

---

## 🚀 How to Run

1.  Start the application.
2.  Check the current pool stats:
    ```bash
    curl http://localhost:8080/debug-application/scenario122/stats
    ```
3.  Observe a connection leak (check terminal logs):
    ```bash
    curl http://localhost:8080/debug-application/scenario122/leak
    ```

## 📝 Interview Tip
> "Why is HikariCP considered the fastest connection pool?"
>
> HikariCP uses a very lean codebase with bytecode-level optimizations and custom collections (like `FastList`) to minimize overhead. It also performs extensive locking optimizations to ensure minimal contention even under high concurrency.
