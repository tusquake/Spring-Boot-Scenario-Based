# JPA Specifications & Dynamic Queries — Complete Interview Reference

## Table of Contents
1. [What are JPA Specifications?](#1-what-are-specifications)
2. [Why Use Specifications? (Static vs Dynamic)](#2-why-use-them)
3. [The Criteria API Under the Hood](#3-criteria-api)
4. [Building a Simple Specification](#4-building-spec)
5. [The Classic Interview Trap: Boolean Logic (AND vs OR)](#5-the-classic-interview-trap-logic)
6. [Combining Specifications (and(), or(), not())](#6-combining)
7. [Joins in Specifications (Fetching related data)](#7-joins)
8. [Specifications with Pagination and Sorting](#8-pagination)
9. [Query by Example (Alternative to Specifications)](#9-query-by-example)
10. [Performance: Query Plan Caching](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What are JPA Specifications?
JPA Specifications (based on the DDD pattern) are a way to write reusable and combinable query predicates using the JPA Criteria API.

---

## 2. Why Use Specifications?
- **Static Queries**: `@Query("SELECT u FROM User u WHERE u.name = ?1")`. Good for fixed logic.
- **Dynamic Queries**: "Search for users where name is X AND/OR email is Y AND/OR age > Z". Specifications allow you to build these queries programmatically based on user input.

---

## 3. The Criteria API Under the Hood
Specifications use the **Criteria API**, which is a typesafe way to write queries in Java. It prevents syntax errors and SQL injection because the query is built using Java objects (`Root`, `CriteriaQuery`, `CriteriaBuilder`).

---

## 4. Building a Simple Specification
A specification is a functional interface that takes three arguments:
- `Root<T>`: The entity you are querying.
- `CriteriaQuery<?>`: The query context.
- `CriteriaBuilder`: The tool to create predicates (like `equal`, `like`, `greaterThan`).

---

## 5. The Classic Interview Trap: Boolean Logic
**The Trap**: You have three optional filters. If all are `null`, what does the query do?
**The Answer**: If you return an empty `and()` predicate, Hibernate generates a query with no `WHERE` clause, returning **ALL** records. You must ensure you have default behavior if no filters are provided.

---

## 6. Combining Specifications
Spring Data JPA provides a fluent API to combine specifications:
```java
customerRepository.findAll(Specification.where(spec1).and(spec2).or(spec3));
```

---

## 7. Joins in Specifications
You can perform joins to filter based on related entities:
```java
(root, query, cb) -> cb.equal(root.join("address").get("city"), "New York");
```

---

## 8. Specifications with Pagination
`JpaSpecificationExecutor` (which your repository must extend) supports `findAll(Specification, Pageable)`. This allows you to combine dynamic filtering with high-performance pagination.

---

## 9. Query by Example (QBE)
QBE is a simpler alternative where you provide a "probe" entity with some fields filled in. Spring then finds all records that match those fields. It's easier than Specifications but lacks support for ranges (e.g., `age > 18`) or complex OR logic.

---

## 10. Performance: Query Plan Caching
Dynamic queries can lead to many unique SQL strings, which can fill up Hibernate's Query Plan Cache. Try to use parameters rather than hardcoding values into the criteria.

---

## 11. Common Mistakes
1. Not extending `JpaSpecificationExecutor` in the repository.
2. Forgetting that `cb.like()` is case-sensitive (use `cb.lower()` for cross-database consistency).
3. Writing complex logic inside the specification that could be done more simply with a DTO.

---

## 12. Quick-Fire Interview Q&A
**Q: Is Criteria API faster than JPQL?**  
A: No. Both are converted to SQL. The performance is the same.  
**Q: When should I use @Query vs Specifications?**  
A: Use `@Query` for fixed, complex SQL. Use `Specifications` for dynamic filters coming from a UI search form.
