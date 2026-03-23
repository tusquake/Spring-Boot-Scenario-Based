# Scenario 92: Read/Write DataSource Splitting

## Overview
**Read/Write Splitting** is a performance optimization pattern where database operations are divided into two categories:
1.  **Writes** (Insert, Update, Delete): Handled by the **Primary** (Source) database.
2.  **Reads** (Select): Handled by one or more **Replica** databases.

In Spring, this is achieved by routing the request based on the `@Transactional(readOnly = true)` attribute.

---

## 🏗️ Core Components

### 1. `RoutingContextHolder`
Uses `ThreadLocal<DataSourceType>` to store whether the current operation is intended for the `WRITE` (Primary) or `READ_ONLY` (Replica) database.

### 2. `ReadOnlyAspect`
An AOP Aspect that intercepts methods with the `@Transactional` annotation.
- If `readOnly = true`, it sets the context to `READ_ONLY`.
- Otherwise, it sets it to `WRITE`.
- **Note**: It uses `@Order(0)` to ensure it runs *before* Spring's transaction manager starts the transaction.

### 3. `LazyConnectionDataSourceProxy`
**CRITICAL COMPONENT**: In standard Spring, the `DataSource` is often asked for a connection *before* the transaction metadata is known. Wrapping our router in a `LazyConnectionDataSourceProxy` defers the connection acquisition until the first actual SQL query is executed, ensuring our Aspect has time to set the routing context.

---

## 🛠️ Implementation Details

### Routing Logic:
```java
public class ReadWriteRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingContextHolder.get(); // Returns WRITE or READ_ONLY
    }
}
```

### Configuration:
- **Primary Database**: `jdbc:h2:mem:primarydb`
- **Replica Database**: `jdbc:h2:mem:replicadb`

---

## 🧪 Testing the Routing

Use these `curl` commands to verify that the application automatically switches databases based on the transaction type.

### Step 1: Verify Write (Primary)
```bash
curl http://localhost:8080/debug-application/api/scenario92/write
```
*Expected: `{"mode": "WRITE", "database": "PRIMARYDB"}`*

### Step 2: Verify Read (Replica)
```bash
curl http://localhost:8080/debug-application/api/scenario92/read
```
*Expected: `{"mode": "READ_ONLY", "database": "REPLICADB"}`*

---

## Interview Tip 💡
**Q**: *"Why do you need `LazyConnectionDataSourceProxy`?"*  
**A**: *"Because the `TransactionManager` typically acquires a connection as soon as the `@Transactional` method is entered. At that point, our custom routing Aspect may not have finished setting the ThreadLocal context. The `LazyConnectionDataSourceProxy` ensures that the `determineCurrentLookupKey()` method is called only when the first SQL is actually executed."*

**Q**: *"Can I use this for multiple replicas?"*  
**A**: *"Yes! You can extend the `determineCurrentLookupKey()` logic to implement a Load Balancer (like Round-Robin) across multiple Replica keys."*
