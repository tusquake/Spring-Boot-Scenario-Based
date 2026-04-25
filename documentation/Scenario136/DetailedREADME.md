# Distributed Scheduling with ShedLock — Complete Interview Reference

## Table of Contents
1. [What is ShedLock?](#1-introduction)
2. [Why Not Just @Scheduled?](#2-why-shedlock)
3. [How ShedLock Works (The Lock Table)](#3-how-it-works)
4. [Configuring @EnableSchedulerLock](#4-configuration)
5. [The Classic Interview Trap: Clock Skew](#5-the-classic-interview-trap-clock-skew)
6. [lockAtMostFor vs lockAtLeastFor](#6-lock-durations)
7. [JDBC vs Redis Lock Providers](#7-lock-providers)
8. [Handling Application Crashes](#8-crashes)
9. [ShedLock vs Quartz](#9-shedlock-vs-quartz)
10. [Using ShedLock with Multiple DataSources](#10-multiple-datasources)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is ShedLock?
ShedLock is a library that ensures your scheduled tasks are executed at most once at the same time. If a task is being executed on one node, it acquires a lock that prevents execution on other nodes.

---

## 2. Why Not Just @Scheduled?
Spring's `@Scheduled` annotation is local to the JVM. If you run 3 instances of your microservice, each instance will run the task independently, leading to duplicate work (e.g., sending the same report 3 times).

---

## 3. How It Works
ShedLock uses an external store (like a database table or Redis) to keep track of locks. Before running a task, the node attempts to update a row in the `shedlock` table. If it succeeds, it has the lock and runs the task.

---

## 4. Configuration
```java
@Scheduled(cron = "0 0 * * * *")
@SchedulerLock(name = "myTask", lockAtMostFor = "10m", lockAtLeastFor = "1m")
public void runTask() { ... }
```

---

## 5. The Classic Interview Trap: Clock Skew
**The Trap**: A user says, *"I use ShedLock, but my task still ran twice!"*
**The Problem**: If Node A's clock is 5 minutes ahead of Node B's clock, they might both think the lock has expired based on their local time.
**The Fix**: Use `.usingDbTime()` in your `LockProvider` configuration. This forces all nodes to use the database server's time for lock logic, eliminating clock skew issues.

---

## 6. Lock Durations
- **lockAtMostFor**: Safety net. If a node dies while holding the lock, it will be released automatically after this time.
- **lockAtLeastFor**: Ensures the lock is held for at least X time, even if the task finishes in 1 second. This prevents multiple executions if the nodes' clocks are slightly out of sync or if the task is very fast.

---

## 7. Lock Providers
- **JdbcTemplateLockProvider**: (Most common) Uses a simple SQL table.
- **RedisLockProvider**: Faster, but requires a Redis instance.
- **MongoLockProvider / ZooKeeperLockProvider**: Other alternatives based on your infrastructure.

---

## 8. Resilience
If the database goes down, ShedLock defaults to "fail-safe" mode. It won't be able to acquire the lock, so the task won't run, preventing duplicate execution until the DB is back.

---

## 9. ShedLock vs Quartz
- **ShedLock**: Lightweight. Good for preventing duplicate `@Scheduled` tasks. It doesn't handle complex scheduling logic (like "run on the 3rd Tuesday").
- **Quartz**: Heavyweight. A full-featured job scheduler with persistence, clustering, and complex triggers.

---

## 10. Monitoring
You can monitor the `shedlock` table directly to see which node currently holds which lock and when they are expected to expire.

---

## 11. Common Mistakes
1. Not creating the `shedlock` table before starting the app.
2. Setting `lockAtMostFor` shorter than the actual task execution time (allowing another node to start the task while it's still running).
3. Using the same `name` for two different tasks.

---

## 12. Quick-Fire Interview Q&A
**Q: Does ShedLock work with fixedRate and fixedDelay?**  
A: Yes, it works with all types of Spring scheduling.  
**Q: What is the primary benefit of ShedLock?**  
A: It's the easiest way to make Spring's `@Scheduled` tasks "Cluster-aware" without moving to a complex scheduler like Quartz.
