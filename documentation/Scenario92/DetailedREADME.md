# Read-Write Data Source Splitting — Complete Interview Reference

## Table of Contents
1. [What is Read-Write Splitting?](#1-what-is-splitting)
2. [Why Split Data Sources? (Scalability)](#2-why-split)
3. [The Role of @Transactional(readOnly = true)](#3-readonly-attribute)
4. [Master-Slave Architecture Overview](#4-master-slave)
5. [The Classic Interview Trap: Replication Lag](#5-the-classic-interview-trap-lag)
6. [Implementing AbstractRoutingDataSource](#6-implementation)
7. [Intercepting @Transactional with AOP](#7-aop-interceptor)
8. [Configuring Multiple DataSources in Spring Boot](#8-configuration)
9. [Handling Transactions across both DataSources](#9-transactions)
10. [Health Checks for Replicas](#10-health-checks)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Read-Write Splitting?
Read-Write splitting is a database architecture where write operations (INSERT, UPDATE, DELETE) are sent to a "Primary" (Master) database, while read operations (SELECT) are distributed across one or more "Replica" (Slave) databases.

---

## 2. Why Split?
Most web applications are "read-heavy" (e.g., 90% reads, 10% writes). By offloading reads to replicas, you can scale your database horizontally and ensure that heavy reporting queries don't slow down the main transaction flow.

---

## 3. @Transactional(readOnly = true)
Spring's `@Transactional` annotation has a `readOnly` flag. When set to `true`, it hints to the application that it can use a read-optimized connection. We can use this flag to decide which datasource to route to.

---

## 4. Master-Slave Architecture
The Primary database is the "Source of Truth". Replicas asynchronously copy data from the Primary. This means there is usually a tiny delay (milliseconds) between a write on the Primary and its availability on the Replica.

---

## 5. The Classic Interview Trap: Read-Your-Own-Writes
**The Trap**: A user creates a post and is immediately redirected to the "Post Detail" page.
**The Problem**: If the "Create" goes to Primary and the "Detail" goes to Replica, the user might see a 404 because of **Replication Lag**.
**The Fix**: You can either:
1. Always route the first read after a write to the Primary.
2. Use a "Session Pinning" strategy where a user's requests stay on the Primary for a few seconds after a write.

---

## 6. AbstractRoutingDataSource
This is the core Spring class used for splitting. You provide a map of DataSources (e.g., `MASTER`, `SLAVE`) and implement the `determineCurrentLookupKey()` method to return the key based on the current transaction status.

---

## 7. Using AOP for Routing
You can use an AOP Aspect that intercepts methods marked with `@Transactional`. If `readOnly = true`, it sets a thread-local variable to `SLAVE`; otherwise, it sets it to `MASTER`.

---

## 8. Configuration
In Spring Boot, you define multiple datasource properties:
`spring.datasource.master.url=...`
`spring.datasource.slave.url=...`
You then manually create the `DataSource` beans and wrap them in your `RoutingDataSource`.

---

## 9. Transactions
Note that a single Spring transaction cannot span across two physical datasources without using "Distributed Transactions" (JTA/XA). Usually, if you start a transaction on the Master, all subsequent reads in that same transaction will also go to the Master.

---

## 10. Health Checks
Your application should be "Replica-aware". If a replica goes down, the routing logic should be smart enough to fallback to the Master or another healthy replica.

---

## 11. Common Mistakes
1. Forgetting to set `readOnly = true` on search/list methods (wasting Primary resources).
2. Not handling replication lag in the business logic.
3. Using only one replica, creating a new single point of failure.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I have multiple replicas?**  
A: Yes, you can use a Round-Robin or Load-Balancing strategy to distribute reads across many replicas.  
**Q: Does read-write splitting improve write performance?**  
A: Indirectly yes, because the Primary database is no longer burdened with read traffic, allowing it to focus on processing writes.
