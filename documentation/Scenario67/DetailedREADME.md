# Saga Pattern (Orchestration & Compensation) — Complete Interview Reference

## Table of Contents
1. [What is the Saga Pattern?](#1-what-is-the-saga-pattern)
2. [Distributed Transactions vs Saga](#2-saga-vs-2pc)
3. [Orchestration vs Choreography](#3-orchestration-vs-choreography)
4. [Compensating Transactions](#4-compensating-transactions)
5. [The Classic Interview Trap: Data Consistency (ACID vs BASE)](#5-the-classic-interview-trap-consistency)
6. [Designing a Saga Orchestrator](#6-saga-orchestrator)
7. [Handling Timeouts and Retries in Sagas](#7-timeouts-retries)
8. [The Saga Log (State Tracking)](#8-saga-log)
9. [Idempotency in Saga Participants](#9-idempotency)
10. [Saga vs Two-Phase Commit (2PC)](#10-saga-vs-2pc)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is the Saga Pattern?
A Saga is a sequence of local transactions. Each local transaction updates the database and publishes a message or event to trigger the next local transaction in the saga. If a local transaction fails because it violates a business rule then the saga executes a series of compensating transactions that undo the changes that were made by the preceding local transactions.

---

## 2. Saga vs 2PC
- **2PC (Two-Phase Commit)**: A "blocking" protocol that ensures all-or-nothing consistency across multiple databases. It doesn't scale well and has high latency.
- **Saga**: Provides "Eventual Consistency". It is much more scalable but requires more complex logic to handle failures.

---

## 3. Orchestration vs Choreography
- **Choreography**: Each service knows what to do next by listening to events. (Decoupled, but hard to track the whole flow).
- **Orchestration**: A central service (the Orchestrator) tells each participant what to do and when. (Easier to manage, but creates a central point of failure).

---

## 4. Compensating Transactions
A compensating transaction is NOT a "Rollback". It is a new transaction that undoes the *business effect* of a previous one. For example, if a "Charge Credit Card" transaction succeeded but the "Book Flight" failed, the compensating transaction would be "Refund Credit Card".

---

## 5. The Classic Interview Trap: Intermediate States
**The Question**: *"What happens if a user checks their balance while a Saga is in progress?"*
**The Answer**: Unlike ACID transactions, Sagas lack **Isolation**. A user might see their money "gone" from the bank before the order is actually confirmed. This is known as the "Dirty Read" problem in distributed systems.

---

## 6. Designing an Orchestrator
The orchestrator is a state machine. It keeps track of where the order is (e.g., `PAYMENT_PENDING`, `INVENTORY_RESERVED`) and handles the logic for jumping to the next step or starting the compensation flow.

---

## 7. Timeouts and Retries
Participants in a Saga must be prepared to be called multiple times (retries) and should have timeouts. If a participant doesn't respond, the orchestrator must decide whether to retry or start the "Undo" process.

---

## 8. The Saga Log
To survive server restarts, the orchestrator must persist its state in a "Saga Log" or database. This ensures that if the orchestrator crashes, it can pick up right where it left off.

---

## 9. Idempotency
**CRITICAL**: Every participant in a Saga must be **Idempotent**. Since the orchestrator might retry a call due to a network timeout, the participant must ensure that calling the same operation twice with the same ID doesn't cause duplicate changes.

---

## 10. BASE Consistency
Sagas follow the **BASE** model:
- **B**asically **A**vailable
- **S**oft-state
- **E**ventual consistency

---

## 11. Common Mistakes
1. Not making compensating transactions idempotent.
2. Trying to implement a Saga for a simple request-response that could be a single DB transaction.
3. Not handling the "lost update" or "dirty read" scenarios in the business logic.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the main benefit of Orchestration over Choreography?**  
A: It is easier to understand and debug because the entire business flow is defined in one place.  
**Q: Can a compensating transaction fail?**  
A: Yes! This is a nightmare scenario. You usually need manual intervention or a persistent retry queue to handle failed compensations.
