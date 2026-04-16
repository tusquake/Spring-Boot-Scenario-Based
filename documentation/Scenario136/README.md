# Scenario 136: Database Indexing Performance Tuning

In high-scale systems, the difference between an indexed and an unindexed query is the difference between an application that scales and one that crashes under load.

### The Problem
When a database column is not indexed, the database must perform a **Full Table Scan (Sequential Scan)**. This means it must read every single row in the table to find the one you are looking for. 

- **Complexity**: $O(n)$
- **Symptoms**: High CPU usage, high I/O, and linear increase in latency as the table grows.

### The Solution: B-Tree Indexes
A B-Tree (Balanced Tree) index organizes data in a tree structure that allows the database to find records in logarithmic time.

- **Complexity**: $O(\log n)$
- **Benefits**: Sub-millisecond lookups even with millions of rows.

---

### 🚀 Debug Challenge

1.  **Seed the Data**: Call the populate endpoint to create 100,000 records.
    ```bash
    curl -X POST "http://localhost:8080/debug-application/api/scenario136/populate?count=100000"
    ```
2.  **Measure performance (Broken State)**: Search for a tracking ID and note the `durationMs`.
    ```bash
    curl "http://localhost:8080/debug-application/api/scenario136/search?trackingId=any-random-uuid"
    ```
    *Expectation*: In a local H2 or Postgres instance with 100k records, this will take significant time (e.g., 50ms - 200ms).

3.  **The Fix**: Apply the index.
    Open `UserActivity.java` and uncomment the `@Index` line:
    ```java
    @Table(name = "user_activities", indexes = {
        @Index(name = "idx_tracking_id", columnList = "tracking_id")
    })
    ```
    *Note: In a production system, you would typically do this via a Flyway migration:*
    ```sql
    CREATE INDEX idx_tracking_id ON user_activities(tracking_id);
    ```

4.  **Verify performance (Fixed State)**: Restart the application and run the search again.
    *Expectation*: The `durationMs` should drop to **0ms or 1ms**.

---

### 💡 Key Takeaways
*   **Cardinality**: Indexes are most effective on high-cardinality columns (columns with many unique values, like UUIDs or emails).
*   **Write Penalty**: Every index makes `INSERT`, `UPDATE`, and `DELETE` operations slightly slower because the index must be updated as well. Don't index everything.
*   **Explain Plan**: Always use `EXPLAIN ANALYZE` (Postgres/MySQL) to see if your query is actually using the index.
