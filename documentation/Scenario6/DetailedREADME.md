# Concurrency & Database Locking — Complete Interview Reference

## Table of Contents
1. [What is Concurrency Control?](#1-what-is-concurrency-control)
2. [Optimistic Locking (@Version)](#2-optimistic-locking)
3. [Pessimistic Locking (FOR UPDATE)](#3-pessimistic-locking)
4. [Optimistic vs Pessimistic Comparison](#4-comparison)
5. [The Classic Interview Trap: The Lost Update Problem](#5-the-classic-interview-trap-the-lost-update-problem)
6. [Handling OptimisticLockException](#6-handling-exception)
7. [Deadlocks: Detection and Prevention](#7-deadlocks)
8. [Locking in Distributed Systems (Redis/Zookeeper)](#8-distributed-locking)
9. [Isolation Levels vs Locking](#9-isolation-levels)
10. [Performance Impact of Heavy Locking](#10-performance-impact)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Concurrency Control?

When multiple users (or threads) try to update the same piece of data at the same time, you face **data inconsistency**. Concurrency control ensures that the final state of the data is correct, even when many updates happen simultaneously.

---

## 2. Optimistic Locking (@Version)

### Definition
Optimistic locking assumes that multiple transactions can complete without affecting each other. It only checks for conflicts when **saving** the data.

### Implementation
In JPA, use the `@Version` annotation on a field (usually `Integer` or `Long`).

```java
@Entity
public class Account {
    @Id private Long id;
    private Double balance;

    @Version
    private Integer version; // Spring/JPA manages this automatically
}
```

### How it works
1.  Transaction A reads the record (Version 1).
2.  Transaction B reads the same record (Version 1).
3.  Transaction A saves. JPA checks: `UPDATE ... WHERE id = ? AND version = 1`. Success! Version is now 2.
4.  Transaction B tries to save. JPA checks: `UPDATE ... WHERE id = ? AND version = 1`. **Fail!** No rows updated.
5.  JPA throws `OptimisticLockException`.

---

## 3. Pessimistic Locking (FOR UPDATE)

### Definition
Pessimistic locking assumes the worst — that conflicts **will** happen. It "locks" the record at the database level as soon as it is read, preventing others from even reading/writing it until the lock is released.

### Implementation
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT a FROM Account a WHERE a.id = :id")
Optional<Account> findByIdWithLock(Long id);
```

---

## 5. The Classic Interview Trap: The Lost Update Problem

### The Problem
Two users, Alice and Bob, both see a balance of $1000.
1.  Alice withdraws $100 (expects $900).
2.  Bob withdraws $200 (expects $800).
3.  Without locking, if Alice saves first and then Bob saves, Bob's save will overwrite Alice's. The final balance becomes $800. Alice's $100 withdrawal is **lost**.

### The Solution
Use **Optimistic Locking** to detect that the data Alice changed has been modified before Bob could save, or **Pessimistic Locking** to make Bob wait until Alice is finished.

---

## 12. Quick-Fire Interview Q&A

**Q: When should I use Optimistic Locking over Pessimistic?**
A: Use **Optimistic** when conflicts are rare (low contention). It has better performance because it doesn't hold DB locks. Use **Pessimistic** when conflicts are frequent (high contention) or when the cost of a failed transaction is very high (e.g., complex financial calculations).

**Q: Does @Version work with native SQL queries?**
A: No. `@Version` is a JPA/Hibernate feature. If you use `jdbcTemplate` or a native query, you must manually increment the version and check the update count.

**Q: What is a Deadlock?**
A: A situation where Transaction A holds Lock 1 and waits for Lock 2, while Transaction B holds Lock 2 and waits for Lock 1. Neither can proceed.

**Q: How do you handle an OptimisticLockException?**
A: Usually by catching it and:
1.  Retrying the operation (reload data, apply changes again).
2.  Notifying the user that the data was updated by someone else.
