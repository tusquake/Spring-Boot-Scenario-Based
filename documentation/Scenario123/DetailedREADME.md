# Java Deadlocks — Complete Interview Reference

## Table of Contents
1. [What is a Deadlock?](#1-what-is-deadlock)
2. [The Four Conditions for Deadlock (Coffman)](#2-coffman-conditions)
3. [Visualizing a Circular Wait](#3-circular-wait)
4. [Deadlocks in Multithreaded Java](#4-java-deadlocks)
5. [The Classic Interview Trap: Lock Ordering](#5-the-classic-interview-trap-ordering)
6. [Detecting Deadlocks with jstack](#6-jstack)
7. [Detecting Deadlocks with Actuator (/threaddump)](#7-actuator)
8. [Deadlock Prevention Strategies](#8-prevention)
9. [Try-Lock: Time-based Lock Acquisition](#9-try-lock)
10. [Deadlocks in Database Transactions](#10-db-deadlocks)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Deadlock?
A deadlock is a situation where two or more threads are blocked forever, each waiting for the other to release a resource.

---

## 2. Coffman Conditions
For a deadlock to occur, four conditions must hold simultaneously:
1. **Mutual Exclusion**: At least one resource is held in a non-sharable mode.
2. **Hold and Wait**: A thread holds one resource and is waiting for another.
3. **No Preemption**: Resources cannot be forcibly taken from a thread.
4. **Circular Wait**: Thread A waits for B, who waits for C, who waits for A.

---

## 3. Circular Wait
The most common cause. 
- Thread 1: Lock A -> Lock B
- Thread 2: Lock B -> Lock A
If they execute at the same time, T1 gets A, T2 gets B, and both hang.

---

## 4. Java Locks
Deadlocks usually occur when using the `synchronized` keyword or `ReentrantLock`.

---

## 5. The Classic Interview Trap: Fixed Lock Ordering
**The Trap**: You have a `transfer(Account from, Account to)` method.
**The Problem**: If two users transfer money to each other at the same time, a deadlock occurs.
**The Fix**: Impose a global order on the locks. For example, always lock the account with the smaller `id` first, regardless of which one is "from" or "to".

---

## 6. Using jstack
`jstack <PID>` is a command-line tool that prints the thread dump. It is smart enough to find deadlocks and explicitly print: `Found 1 deadlock.`

---

## 7. Spring Boot Actuator
The `/actuator/threaddump` endpoint returns a JSON representation of all threads. Many monitoring tools (like Prometheus/Grafana) use this to alert on deadlocks in production.

---

## 8. Prevention
- **Lock Ordering**: Always acquire locks in the same order.
- **Lock Timeout**: Use `tryLock(timeout, unit)` instead of `lock()`.
- **Minimize Scope**: Hold locks for the shortest time possible.

---

## 9. Try-Lock
If a thread cannot get a lock within a certain time, it should back off, release any locks it currently holds, and try again later. This breaks the "Circular Wait" condition.

---

## 10. Database Deadlocks
Databases (like MySQL/PostgreSQL) have built-in deadlock detectors. When they find a circular wait between two transactions, they will forcibly roll back one of them to let the other proceed.

---

## 11. Common Mistakes
1. Acquiring multiple locks without a clear strategy.
2. Holding a lock while performing a slow network or I/O operation.
3. Not using a timeout for lock acquisition.

---

## 12. Quick-Fire Interview Q&A
**Q: How do you fix a deadlock in production?**  
A: Restarting the application is the only immediate fix. You then use the thread dump to identify the code path and implement lock ordering.  
**Q: What is a "Livelock"?**  
A: A situation where threads keep changing their state in response to each other, but none of them make any progress (like two people trying to pass each other in a hallway).
