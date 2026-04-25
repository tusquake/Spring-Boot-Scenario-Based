# Spring Data JPA Queries — Complete Interview Reference

## Table of Contents
1. [Derived Query Methods](#1-derived-queries)
2. [What is @Query? (JPQL)](#2-jpql-query)
3. [Native Queries vs JPQL](#3-native-vs-jpql)
4. [Using Named Parameters (:param)](#4-named-parameters)
5. [The Classic Interview Trap: Updating Data (@Modifying)](#5-the-classic-interview-trap-modifying)
6. [Pagination and Sorting in @Query](#6-pagination-sorting)
7. [Projection (Interface-based vs DTO-based)](#7-projections)
8. [Dynamic Queries with Specifications](#8-specifications)
9. [Entity Graphs in @Query](#9-entity-graphs)
10. [Performance: Query Plan Cache](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Derived Query Methods
Spring Data JPA can automatically generate SQL queries by parsing method names.
- `findByAuthorContainingIgnoreCase(String name)`
- `findTop3ByPriceLessThan(Double price)`
- `countByPublishedDateAfter(LocalDate date)`

---

## 2. What is @Query? (JPQL)
The `@Query` annotation allows you to write custom queries using **JPQL** (Java Persistence Query Language). JPQL is platform-independent because it targets your Entity classes and fields, not the database tables.

---

## 3. Native Queries
Use `nativeQuery = true` to write raw SQL that is specific to your database (e.g., PostgreSQL or Oracle).
**Pros**: Full access to database features (like JSONB or CTEs).
**Cons**: You lose database portability.

---

## 4. Named Parameters
Instead of positional parameters (`?1`), use named parameters (`:authorName`) for better readability and to avoid errors if the parameter order changes.

---

## 5. The Classic Interview Trap: @Modifying
**The Trap**: You write an `UPDATE` or `DELETE` query in `@Query`.
**The Problem**: Spring Data will throw an exception if you don't include the `@Modifying` annotation.
**The Fix**:
```java
@Modifying
@Transactional
@Query("UPDATE Book b SET b.price = b.price * :factor")
int updatePrices(double factor);
```
**Bonus**: If you want the Persistence Context (L1 Cache) to be updated after the query, add `clearAutomatically = true` to `@Modifying`.

---

## 6. Pagination in @Query
You can simply add a `Pageable` argument to any `@Query` method, and Spring Data will automatically append the correct `LIMIT` and `OFFSET` clauses.

---

## 7. Projections
If you don't need the entire Entity, use Projections to fetch only specific columns.
- **Interface Projections**: Define an interface with `get` methods.
- **DTO Projections**: Use `SELECT new com.app.MyDto(u.name, u.email) FROM User u`.

---

## 8. Dynamic Queries
For complex search screens with many optional filters, derived queries and `@Query` become messy. Use **JPA Specifications** (Criteria API) to build queries programmatically at runtime.

---

## 9. Entity Graphs
Use `@EntityGraph` alongside `@Query` to avoid the "N+1 Problem" by specifying which associations should be fetched eagerly for that specific query.

---

## 10. Performance
Derived queries are parsed at application startup. Complex queries with many joins should be profiled using `EXPLAIN ANALYZE` to ensure they are using the correct indexes.

---

## 11. Common Mistakes
1. Using string concatenation inside `@Query` (leads to SQL Injection).
2. Forgetting `@Modifying` for DML operations.
3. Using `nativeQuery` for simple operations that JPQL can handle (losing portability).

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @Query on a default method in a Repository interface?**  
A: No, the annotation must be on the method declaration itself.  
**Q: What is the benefit of JPQL over Native SQL?**  
A: It's type-safe against your entity model and works across different database providers.
