# Scenario 129: Transaction Isolation Levels

Isolation levels define how a transaction is isolated from the data modifications made by other concurrent transactions. This scenario demonstrates the three classic concurrency phenomena and how Java/Spring handles them.

## High-Level Concepts

### 1. Dirty Read
- **Phenomenon**: A transaction reads data that has been modified by another transaction but not yet committed. If the first transaction rolls back, the second one has "dirty" data.
- **Fix**: Use `Isolation.READ_COMMITTED`.

### 2. Non-Repeatable Read
- **Phenomenon**: A transaction reads the same row twice and gets different data because another transaction updated and committed that row in between.
- **Fix**: Use `Isolation.REPEATABLE_READ`.

### 3. Phantom Read
- **Phenomenon**: A transaction performs a range query (e.g., `count(*)`) twice and gets different results because another transaction inserted/deleted rows in that range.
- **Fix**: Use `Isolation.SERIALIZABLE`.

---

## ⚠️ The H2 "Snapshot" Surprise
In this scenario, you might notice that H2 prevents all three phenomena even at the lowest isolation levels. This is because **H2 uses MVCC (Multi-Version Concurrency Control)** by default. 
- In MVCC, a transaction sees a "consistent snapshot" of the data from the moment it began. 
- Updates and inserts by other transactions are hidden until the next transaction begins.

---

## How to Test this Scenario

### 1. Dirty Read Demo (Vulnerable)
`GET http://localhost:8080/debug-application/api/scenario129/dirty-read?fixed=false`
- **T1** updates Alice's balance to 9999 and stalls.
- **T2** tries to read it using `READ_UNCOMMITTED`.
- **Observation**: T2 still reads 1000.0 (H2 prevents dirty reads in MVCC mode).

### 2. Non-Repeatable Read Demo
`GET http://localhost:8080/debug-application/api/scenario129/non-repeatable-read?fixed=false`
- **T1** reads Alice (1000.0).
- **T2** updates and commits Alice (2000.0).
- **T1** reads Alice again.
- **Observation**: T1 reads 1000.0 again (Snapshot Isolation).

### 3. Phantom Read Demo
`GET http://localhost:8080/debug-application/api/scenario129/phantom-read?fixed=false`
- **T1** counts accounts (2).
- **T2** inserts "Charlie" and commits.
- **T1** counts again.
- **Observation**: T1 still counts 2 (Snapshot Isolation).

---

## 🚀 Key Learning
*   **Isolation Levels are Minimums**: The SQL standard defines the **minimum** phenomena an isolation level must prevent. A database is allowed to be stricter than requested (as H2 is here).
*   **Performance vs Consistency**: Higher isolation levels (like `SERIALIZABLE`) provide the most consistency but have the lowest performance due to locking.
*   **Spring Propagation**: Remember that `@Transactional` defaults to `READ_COMMITTED`.
