# Scenario 67: Saga Pattern (Distributed Transactions)

[Note: Demonstrated via Scenario67Controller at /api/scenario67]

In a microservices world, you can't use a single `@Transactional` across different databases. The **Saga Pattern** is the solution for maintaining data consistency.

## Orchestration vs Choreography

| Feature | Orchestration (Used here) | Choreography |
| :--- | :--- | :--- |
| **Concept** | A central "Brain" (Orchestrator) tells everyone what to do. | Each service listens to events and decides what to do. |
| **Complexity** | Centralized, easier to track state. | Decentralized, can become "Spaghetti" events. |
| **Coupling** | Orchestrator knows all services. | Services only know events (Loosely coupled). |

---

## Compensating Transactions
Since we cannot "Rollback" a database commit that already happened in another service, we must execute a **Compensating Transaction** to undo the previous action.

- **Action**: `Deduct Stock` -> **Compensation**: `Add Stock Back`
- **Action**: `Charge Credit Card` -> **Compensation**: `Refund Money`

---

## 🚀 implementation
This scenario implements an **Orchestration-based Saga** for an e-commerce order:

1. **Step 1**: Payment Service (Reserve Funds)
2. **Step 2**: Inventory Service (Deduct Stock)
3. **If Failure**: The `OrderSagaOrchestrator` calls the compensation methods in reverse order.

### How to Test:
Use the new dedicated route `/api/saga/...`

1. **Success Path**:
   `curl "http://localhost:8080/api/saga/process"`
   - *Logs*: Payment Reserved -> Inventory Deducted -> Completed.

2. **Failure at Inventory (Rollback Payment)**:
   `curl "http://localhost:8080/api/saga/process?failAt=inventory"`
   - *Logs*: Payment Reserved -> Inventory Fails -> **[Compensating] Refunding Payment**.

3. **Failure at Payment**:
   `curl "http://localhost:8080/api/saga/process?failAt=payment"`
   - *Logs*: Payment Fails -> No compensations run.

---

## Interview Questions
- **Q: Why don't we use 2PC (Two-Phase Commit) in Microservices?**
  - **A**: 2PC is synchronous and blocks resources. It doesn't scale well. Sagas are asynchronous and eventually consistent.
- **Q: What happens if a Compensating Transaction fails?**
  - **A**: This is an edge case requiring **Retry logic** or a **Dead Letter Queue (DLQ)** for manual intervention.
