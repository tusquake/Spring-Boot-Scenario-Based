# Scenario 32: Entity Graphs (N+1 Solution)

Demonstrates a more flexible and declarative way to solve the N+1 problem compared to JPQL Join Fetch.

## Concept
In Scenario 9, we used `JOIN FETCH`. While effective, it ties the fetching logic to the query. **Entity Graphs** let you define *how* an entity should be loaded separately from *what* query is used.

## Implementation Details
We compared a standard `findAll()` against a repository method specifically decorated with an `@EntityGraph`.

### Repository Snippet:
```java
@EntityGraph(attributePaths = {"orders"})
@Query("SELECT c FROM Customer c")
List<Customer> findAllWithOrders();
```

## Verification Results
1. **Bad Way**: `/api/scenario32/nplus1` -> Look at logs. You'll see 1 main query + 10 separate queries for orders.
2. **Optimized Way**: `/api/scenario32/optimized` -> Look at logs. You'll see exactly **ONE** efficient `LEFT JOIN` query.

## Interview Theory: Join Fetch vs Entity Graph
- **Flexibility**: You can have multiple graphs for one entity (e.g., `SummaryGraph`, `DetailGraph`) and choose the right one for each repo method.
- **Complexity**: For deeply nested collections (e.g., Customer -> Orders -> Items), Entity Graphs are much cleaner than writing a 5-line JPQL JOIN FETCH string.
- **Cartesian Product**: Warn the interviewer that fetching multiple large collections (Two `@OneToMany` relationships) in a single JOIN FETCH can cause a "Cartesian Product" explosion, slowing down the DB.
