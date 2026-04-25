# JPQL vs Native vs Named Queries — Complete Interview Reference

## Table of Contents
1. [Introduction to Spring Data JPA Queries](#1-introduction)
2. [JPQL: Java Persistence Query Language](#2-jpql)
3. [Native SQL Queries](#3-native-sql)
4. [Named Queries (@NamedQuery)](#4-named-queries)
5. [Named Native Queries (@NamedNativeQuery)](#5-named-native)
6. [The Classic Interview Trap: Startup Validation](#6-the-classic-interview-trap-validation)
7. [Dynamic Queries with @Query](#7-dynamic-queries)
8. [Native Query Mapping with @SqlResultSetMapping](#8-result-mapping)
9. [Performance: JPQL vs Native SQL](#9-performance)
10. [When to use which?](#10-when-to-use)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Spring Data JPA provides multiple ways to define queries. Choosing the right one depends on whether you need database portability, performance, or reusability.

---

## 2. JPQL
**How it works**: You write queries against your **Java Entities** and fields, not database tables and columns.
- **Example**: `SELECT u FROM User u WHERE u.email = :email`
- **Pros**: Database independent. Hibernate translates it to the correct dialect (MySQL, Postgres, etc.).

---

## 3. Native SQL
**How it works**: You write raw SQL.
- **Example**: `SELECT * FROM users WHERE user_email = ?1`
- **Pros**: Access to DB-specific features (like JSON functions, window functions, or spatial queries).

---

## 4. Named Queries
**How it works**: Defined on the `@Entity` class using the `@NamedQuery` annotation.
- **Pros**: They are "compiled" and validated at startup, reducing the risk of runtime errors.

---

## 5. Named Native Queries
Similar to Named Queries but using raw SQL. These are also defined on the `@Entity` class.

---

## 6. The Classic Interview Trap: Validation Timing
**The Trap**: A user asks, *"Why should I use @NamedQuery instead of just @Query in the interface?"*
**The Answer**: **Validation**. Spring Data JPA validates `@Query` (JPQL) when the bean is initialized at startup. However, `@NamedQuery` is validated by Hibernate when the persistence unit is created. Native queries (whether `@Query(native=true)` or `@NamedNativeQuery`) are **NOT** validated until the method is actually called, which can lead to late-discovery bugs.

---

## 7. Dynamic Queries
If you need to add `WHERE` clauses conditionally, you should use the **Criteria API** or **QueryDSL** instead of trying to concatenate strings in `@Query`.

---

## 8. SqlResultSetMapping
When using Native Queries that return something other than an entity (like a complex aggregation), you must use `@SqlResultSetMapping` to tell JPA how to map the raw rows into a DTO or a different object.

---

## 9. Performance
Native SQL is theoretically faster because it bypasses Hibernate's JPQL parser. However, the difference is usually negligible (microseconds) unless the query is extremely complex.

---

## 10. When to use which?
- **@Query (JPQL)**: 90% of the time. Safe and easy.
- **Native Query**: For performance tuning or using database-specific functions.
- **Named Query**: When you want to keep the query logic inside the Entity for better organization.

---

## 11. Common Mistakes
1. Using `SELECT *` in JPQL (In JPQL, you should select the entity alias: `SELECT u FROM User u`).
2. Not using named parameters (`:email`) in Native queries (vulnerable to SQL injection).
3. Hardcoding table names in JPQL (it should be the entity name).

---

## 12. Quick-Fire Interview Q&A
**Q: Does JPQL support JOINs?**  
A: Yes, it supports INNER, LEFT, and RIGHT joins just like SQL.  
**Q: Can I return a DTO from a @Query?**  
A: Yes, using the `SELECT new com.app.dto.MyDto(...)` syntax.
