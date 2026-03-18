# Scenario 9: JPA N+1 Select Problem vs Join Fetch

Demonstrates one of the most common performance issues in hibernate and how to fix it.

## Concept
1. **N+1 Problem**: You fetch $N$ Orders. Then, as you loop through them, Hibernate sends 1 separate query for *each* order to get its Customer. Result: 1 (main query) + $N$ (lazy sub-queries) = $N+1$ total queries.
2. **JOIN FETCH**: A single query that fetches both the Order and the associated Customer in one shot.

## Implementation Details
We compared a standard `findAll()` against a custom `@Query` using `JOIN FETCH`.

### The Problematic Call:
```java
// Triggers N+1 queries
List<Order> orders = orderRepository.findAll();
```

### The Fix:
```java
@Query("SELECT o FROM Order o JOIN FETCH o.customer")
List<Order> findAllWithCustomers();
```

## Verification Results
- **URL (Problem)**: `/api/scenario9/test/n-plus-one`
  - **Check Logs**: You will see many `SELECT` statements for the `customers` table.
- **URL (Fixed)**: `/api/scenario9/test/join-fetch`
  - **Check Logs**: You will see exactly **ONE** query with an `INNER JOIN`.

## Interview Theory: JPA Performance
- **N+1 Identification**: If you hear the phrase "hibernate is slow," N+1 is the #1 suspect.
- **FetchType**: `ManyToOne` is `EAGER` by default in JPA, which can hide N+1 but kill performance. `OneToMany` is `LAZY` by default.
- **Entity Graphs**: Besides `JOIN FETCH`, you can also use `@EntityGraph` to dynamically load associations without writing custom JPQL.
