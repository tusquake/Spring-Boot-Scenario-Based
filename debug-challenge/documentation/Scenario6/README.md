# Scenario 6: Concurrency Control (Race Conditions & Locking)

Demonstrates how to handle multiple users updating the same data at the same time using JPA Locking.

## Concept
1. **Race Condition**: When two threads try to update the same record, and the second one overwrites the first one's change (Lost Update).
2. **Optimistic Locking**: Uses a `@Version` column. The second update fails with `OptimisticLockException` because the version has changed.
3. **Pessimistic Locking**: Locks the database row (`SELECT FOR UPDATE`) so no one else can even read it until the first transaction finishes.

## Implementation Details
We used a `BankService` to simulate concurrent deposits into a single account.

### Optimistic Lock Snippet:
```java
@Lock(LockModeType.OPTIMISTIC)
Optional<Account> findById(Long id);
```

### Pessimistic Lock Snippet:
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT a FROM Account a WHERE a.id = :id")
Optional<Account> findByIdPessimistic(Long id);
```

## Verification Results
1. **Race Condition Test**: `/api/scenario6/test/race-condition`
   - **Result**: You'll see one thread fail with an `OptimisticLockException` in the console.
2. **Safe Update Test**: `/api/scenario6/test/pessimistic`
   - **Result**: Both updates succeed sequentially because the database row was locked.

## Interview Theory: Optimistic vs Pessimistic
- **Optimistic**: Best for **High Read / Low Conflict** environments. It's faster because it doesn't hold DB locks, but the user has to retry if they get a conflict.
- **Pessimistic**: Best for **High Conflict** environments (like a bank transfer or stock trade). It ensures safety but can lead to "Deadlocks" if not handled carefully.
- **The @Version tag**: Mention that JPA's Optimistic locking is usually implemented via an `@Version` integer or timestamp column.
