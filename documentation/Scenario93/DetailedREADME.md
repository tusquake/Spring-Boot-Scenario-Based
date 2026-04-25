# Advanced @Transactional — Complete Interview Reference

## Table of Contents
1. [Introduction to Transactional Management](#1-introduction)
2. [Transaction Propagation: REQUIRED vs REQUIRES_NEW](#2-propagation)
3. [The Classic Interview Trap: Self-Invocation](#3-the-classic-interview-trap-self-invocation)
4. [Checked vs Unchecked Exceptions Rollback](#4-rollback-rules)
5. [Customizing Rollback with rollbackFor](#5-customizing-rollback)
6. [Transaction Isolation Levels (READ_COMMITTED vs REPEATABLE_READ)](#6-isolation-levels)
7. [The N+1 Problem in Transactions](#7-n-plus-one)
8. [Read-Only Transactions Optimization](#8-readonly-optimization)
9. [Transaction Synchronization (AfterCommit logic)](#9-synchronization)
10. [Programmatic Transactions (TransactionTemplate)](#10-programmatic)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Spring's `@Transactional` annotation is the most powerful yet misunderstood feature in the framework. It handles the complexity of `begin`, `commit`, and `rollback` logic behind an AOP proxy.

---

## 2. Transaction Propagation
- **REQUIRED**: (Default) If a transaction exists, join it. If not, create one.
- **REQUIRES_NEW**: Always create a new transaction, suspending the current one if it exists. Useful for logging or auditing where you want the audit to be saved even if the main transaction fails.
- **NESTED**: Starts a nested transaction using "savepoints". If the nested transaction fails, it can be rolled back without affecting the outer transaction.

---

## 3. The Classic Interview Trap: Self-Invocation
**The Trap**: Method A (no annotation) calls Method B (`@Transactional`) inside the same class.
**The Problem**: The transaction will **NOT** start. Spring uses AOP proxies. When you call a method internally, you bypass the proxy, so the transaction interceptor is never triggered.
**The Fix**: Move Method B to a different bean, or inject the bean into itself (Self-Injection).

---

## 4. Rollback Rules
By default, Spring **only** rolls back for **Unchecked Exceptions** (`RuntimeException` and `Error`).
It does **NOT** roll back for **Checked Exceptions** (`IOException`, `SQLException`, etc.).

---

## 5. Customizing Rollback
You can override the default behavior using the `rollbackFor` attribute:
`@Transactional(rollbackFor = Exception.class)`
This ensures that the transaction rolls back for ANY exception, checked or unchecked.

---

## 6. Isolation Levels
- **DEFAULT**: Use the DB's default (usually READ_COMMITTED).
- **READ_COMMITTED**: Prevents dirty reads.
- **REPEATABLE_READ**: Prevents dirty and non-repeatable reads.
- **SERIALIZABLE**: Maximum isolation, prevents all anomalies but has very poor performance.

---

## 7. The N+1 Problem
If you access a lazy-loaded collection inside a `@Transactional` method, Hibernate will fetch the data on demand. If you do this in a loop, it results in many database roundtrips.

---

## 8. Read-Only Optimization
Setting `@Transactional(readOnly = true)` hints to the persistence provider (Hibernate) that it doesn't need to perform "Dirty Checking" on entities. This reduces memory usage and improves speed.

---

## 9. Transaction Synchronization
Sometimes you need to perform an action (like sending an email or a Kafka message) **ONLY** if the transaction succeeds. Use `TransactionSynchronizationManager` or `@TransactionalEventListener`.

---

## 10. Programmatic Transactions
When you need fine-grained control over when a transaction starts or ends within a single method, use `TransactionTemplate` instead of the annotation.

---

## 11. Common Mistakes
1. Using `@Transactional` on `private` methods (Proxies only work on public methods).
2. Catching an exception inside the method and not re-throwing it (Spring won't know to rollback).
3. Long-running tasks inside a transaction (holds DB connections hostage).

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @Transactional with NoSQL?**  
A: Only if the NoSQL database (like MongoDB 4.0+) and its Spring Data driver support multi-document transactions.  
**Q: What is a "Ghost Read" (Phantom Read)?**  
A: When a query returns different rows because another transaction inserted data in between.
