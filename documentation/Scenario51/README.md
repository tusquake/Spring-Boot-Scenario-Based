# Scenario 51: JPA Locking (Optimistic vs Pessimistic)

Demonstrates how to handle data consistency in high-concurrency environments like Banking or Ticketing.

## Concept
1. **Optimistic Locking**: Assumes no conflict. Uses a `@Version` column. If two users update the same row, the second one fails with an `ObjectOptimisticLockingFailureException`.
2. **Pessimistic Locking**: Assumes conflict. Issues a `SELECT ... FOR UPDATE` query, locking the row at the database level so others must wait.

## Implementation Details
We used a `BankAccount` entity and a `withdraw` logic with simulated network delays.

### Optimistic Code:
```java
@Version
private Long version;
```

### Pessimistic Code:
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
Optional<BankAccount> findByIdForUpdate(Long id);
```

## Verification Results
- **Scenario**: Two users try to withdraw $100 simultaneously from an account with $150.
- **Optimistic**: One succeeds. The other gets a "Version Conflict" error. (Better for scale, occasional conflicts).
- **Pessimistic**: The first user locks the row. The second user *waits* for 5 seconds until the first one is done, then succeeds. (Better for high-risk data like money).

## Interview Theory: Which Lock when?
- **Scale**: Optimistic is better for high-traffic apps because it doesn't hold DB connections open.
- **Safety**: Pessimistic is better if the "Cost of Failure" is high (e.g., losing a seat in a stadium).
- **Deadlocks**: Warn the interviewer that Pessimistic locking can lead to Deadlocks if not used carefully.
