# Transaction Isolation Levels — Complete Interview Reference

## Table of Contents
1. [What is Transaction Isolation?](#1-introduction)
2. [The Three Read Phenomena](#2-phenomena)
3. [Dirty Read Explained](#3-dirty-read)
4. [Non-Repeatable Read Explained](#4-non-repeatable-read)
5. [Phantom Read Explained](#5-phantom-read)
6. [The Four Isolation Levels (ANSI SQL)](#6-isolation-levels)
7. [The Classic Interview Trap: Default Levels (MySQL vs Postgres)](#7-the-classic-interview-trap-defaults)
8. [Read Committed: The Most Popular Level](#8-read-committed)
9. [Repeatable Read: How It Works (MVCC)](#9-repeatable-read)
10. [Serializable: Performance vs Safety](#10-serializable)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Isolation?
Isolation is the 'I' in ACID. It ensures that concurrent transactions do not interfere with each other. The higher the isolation, the fewer the anomalies, but the lower the performance (due to locking).

---

## 2. Read Phenomena
- **Dirty Read**: Reading uncommitted data from another transaction.
- **Non-Repeatable Read**: Reading the same row twice and getting different values.
- **Phantom Read**: Reading a range of rows twice and finding new rows (phantoms) the second time.

---

## 3. Dirty Read
If Transaction A updates a row and Transaction B reads it **before** A commits, B has performed a dirty read. If A then rolls back, B is left with data that "never officially existed".

---

## 4. Non-Repeatable Read
Occurs when another transaction **updates or deletes** a row that you have already read. When you read it again, the values have changed.

---

## 5. Phantom Read
Occurs when another transaction **inserts** new rows that match your query's WHERE clause. When you re-run the query, you see more rows than before.

---

## 6. Isolation Levels
| Level | Dirty Read | Non-Repeatable | Phantom |
|---|---|---|---|
| **Read Uncommitted** | Possible | Possible | Possible |
| **Read Committed** | No | Possible | Possible |
| **Repeatable Read** | No | No | Possible |
| **Serializable** | No | No | No |

---

## 7. The Classic Interview Trap: Default Defaults
**The Trap**: A user asks, *"What is the default isolation level in Spring?"*
**The Answer**: Spring doesn't have a default! It uses the default of the **underlying database**.
- **MySQL**: `REPEATABLE_READ`.
- **PostgreSQL / Oracle / SQL Server**: `READ_COMMITTED`.
This is critical because an application moved from MySQL to Postgres might suddenly start experiencing Non-Repeatable Reads.

---

## 8. Read Committed
The standard for most applications. It uses short-term read locks or MVCC (Multi-Version Concurrency Control) to ensure you only ever read data that has been successfully committed.

---

## 9. Repeatable Read (MVCC)
In modern databases like MySQL and Postgres, `REPEATABLE_READ` doesn't actually lock rows. Instead, it takes a "Snapshot" of the database at the start of your transaction. Every query in your transaction reads from that same snapshot, even if other people commit changes in the meantime.

---

## 10. Serializable
The strictest level. It makes transactions behave as if they were running one after another. It is very slow and can cause many "Serialization Failure" errors (Deadlocks) that the application must be prepared to retry.

---

## 11. Common Mistakes
1. Using `SERIALIZABLE` by default "just to be safe" (causing massive performance bottlenecks).
2. Forgetting that `REPEATABLE_READ` does not always prevent Phantom Reads (it depends on the DB engine).
3. Not realizing that isolation levels only apply **inside** a transaction.

---

## 12. Quick-Fire Interview Q&A
**Q: How do I set the isolation level in Spring?**  
A: `@Transactional(isolation = Isolation.READ_COMMITTED)`.  
**Q: What is the most common isolation level?**  
A: `READ_COMMITTED` because it provides the best balance between performance and data consistency.
