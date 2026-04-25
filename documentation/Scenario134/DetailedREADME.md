# Transaction Rollback Rules — Complete Interview Reference

## Table of Contents
1. [Introduction to @Transactional Rollback](#1-introduction)
2. [Checked vs Unchecked Exceptions](#2-exceptions)
3. [Default Rollback Behavior](#3-default-behavior)
4. [Customizing Rollback with rollbackFor](#4-customizing)
5. [The 'noRollbackFor' Attribute](#5-no-rollback)
6. [The Classic Interview Trap: Catching Exceptions](#6-the-classic-interview-trap-catching)
7. [Rollback in Private Methods](#7-private-methods)
8. [Programmatic Rollback (TransactionAspectSupport)](#8-programmatic)
9. [Rollback in Multi-Threaded Scenarios](#9-multi-threaded)
10. [Database vs Application Rollback](#10-database-vs-app)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Transaction management ensures that a series of database operations either all succeed or all fail together. "Rollback" is the mechanism that undoes all changes if something goes wrong.

---

## 2. Checked vs Unchecked
- **Unchecked (Runtime)**: `RuntimeException`, `Error`. These represent fatal or programming errors.
- **Checked**: `Exception`, `IOException`, `SQLException`. These represent "expected" error conditions that the programmer must handle.

---

## 3. Default Behavior
By default, Spring `@Transactional` only rolls back for **Unchecked Exceptions** (`RuntimeException` and its subclasses). It does **NOT** roll back for Checked Exceptions.

---

## 4. rollbackFor
If you want a transaction to roll back when a Checked Exception occurs, you must specify it:
`@Transactional(rollbackFor = Exception.class)`
This is a very common best practice to ensure total data safety.

---

## 5. noRollbackFor
Sometimes an exception is just for information (e.g., `AlreadyExistsException`) and you still want to commit the rest of the work.
`@Transactional(noRollbackFor = SpecificException.class)`

---

## 6. The Classic Interview Trap: Swallowing Exceptions
**The Trap**: You have a `@Transactional` method. Inside, you have a `try-catch` block that catches an exception, logs it, but does **NOT** re-throw it.
**The Problem**: Since the exception never leaves the method, the Spring Transaction Proxy doesn't know anything went wrong. It will proceed to **COMMIT** the transaction.
**The Fix**: Always re-throw the exception or use `TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()` if you must catch it but still want a rollback.

---

## 7. Private Methods
As discussed in previous scenarios, `@Transactional` on a private method (or a method called from within the same class) will not work because the proxy is bypassed. Consequently, no rollback will occur.

---

## 8. Programmatic Rollback
If you are using a `TransactionTemplate` or if you are in a complex logic flow where you want to trigger a rollback without throwing an exception:
`TransactionStatus status = ...; status.setRollbackOnly();`

---

## 9. Multi-threading
Transactions are bound to a **ThreadLocal**. If you start a new thread inside a transactional method, that new thread is **NOT** part of the parent transaction. If the parent rolls back, the changes in the child thread will NOT be undone.

---

## 10. Database Rollback
When Spring triggers a rollback, it sends a `ROLLBACK` command to the database. The database then uses its Undo Logs / Redo Logs to restore the data to its previous state.

---

## 11. Common Mistakes
1. Assuming `rollbackFor = Exception.class` is the default.
2. Catching exceptions and not re-throwing them.
3. Expecting a rollback to undo non-database changes (like sending an email or writing a file).

---

## 12. Quick-Fire Interview Q&A
**Q: Does @Transactional roll back for 'Error' (like OutOfMemoryError)?**  
A: Yes, it rolls back for `java.lang.Error` by default.  
**Q: Can I roll back only a part of a transaction?**  
A: Yes, using "Savepoints", though this is rarely done in Spring; usually, you use nested transactions instead.
