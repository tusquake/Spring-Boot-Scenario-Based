# JPA Projections — Complete Interview Reference

## Table of Contents
1. [What are Projections?](#1-what-are-projections)
2. [Why Use Projections? (The SELECT * Problem)](#2-why-use-them)
3. [Interface-Based Projections (Closed vs Open)](#3-interface-projections)
4. [Class-Based Projections (DTOs)](#4-class-projections)
5. [The Classic Interview Trap: SpEL in Open Projections](#5-the-classic-interview-trap-spel)
6. [Dynamic Projections](#6-dynamic-projections)
7. [Projections with Native Queries](#7-native-projections)
8. [Performance Comparison: Entity vs Projection](#8-performance)
9. [Projections and Pagination](#9-projections-pagination)
10. [Nested Projections (Handling Relationships)](#10-nested-projections)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What are Projections?
Projections allow you to retrieve only a subset of an entity's fields from the database. Instead of fetching a 50-column `User` entity, you can fetch just the `id` and `email`.

---

## 2. Why Use Projections?
- **Network Traffic**: Smaller payloads sent from DB to App.
- **Memory**: Fewer objects in the JVM heap and no "Dirty Checking" overhead for the persistence context.
- **Security**: Prevent leaking sensitive fields (like `password_hash`) to the REST API.

---

## 3. Interface-Based Projections
- **Closed Projections**: The method names match the entity field names. Spring generates an optimized `SELECT id, email ...` query.
- **Open Projections**: Uses the `@Value` annotation with SpEL. Spring fetches the **WHOLE** entity and then extracts the values. This is **NOT** a performance optimization!

---

## 4. Class-Based Projections (DTOs)
You use a simple Java class (Data Transfer Object) with a constructor.
`@Query("SELECT new com.app.UserDto(u.id, u.email) FROM User u")`
This is highly efficient and provides full type safety.

---

## 5. The Classic Interview Trap: Open Projection Performance
**The Trap**: A developer uses `@Value("#{target.firstName + ' ' + target.lastName}")` thinking they are optimizing the query.
**The Problem**: Because SpEL can access any property of the target, Spring must fetch the **entire entity row** from the database to populate the target object. There is no SQL optimization in Open Projections.
**The Fix**: Use Closed Projections or DTOs for true performance gains.

---

## 6. Dynamic Projections
You can create a single repository method that returns different projection types based on the argument:
`<T> List<T> findByLastName(String lastName, Class<T> type);`

---

## 7. Native Projections
When using native SQL, Spring Data JPA can still map the results to an interface projection, provided the column aliases in the SQL match the method names in the interface.

---

## 8. Performance Comparison
For a table with 1 million rows and 50 columns, fetching a projection of 2 columns can be **5x to 10x faster** than fetching the full entities, mostly due to reduced object creation and garbage collection pressure.

---

## 9. Projections and Pagination
Projections work seamlessly with `Pageable`. You just return `Page<MyProjection>` instead of `Page<MyEntity>`.

---

## 10. Nested Projections
You can have a projection that contains another projection (e.g., `UserSummary` containing an `AddressSummary`). Hibernate handles this by using subqueries or joins.

---

## 11. Common Mistakes
1. Using Open Projections for performance optimization.
2. Forgetting that Projections are **Read-Only**; you cannot "save" a projection back to the database.
3. Not using DTOs when the logic becomes too complex for simple interfaces.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @JsonIgnore with Projections?**  
A: No, since Projections are interfaces, you don't need it. Just don't define the method in the interface if you don't want the field.  
**Q: What is a "Closed" projection?**  
A: One where all getter methods match the property names of the entity, allowing Spring to optimize the SQL `SELECT` clause.
