# JPA Inheritance: Single Table Strategy — Complete Interview Reference

## Table of Contents
1. [What is JPA Inheritance?](#1-what-is-inheritance)
2. [Why Use Single Table Strategy?](#2-why-single-table)
3. [The Discriminator Column](#3-discriminator-column)
4. [Polymorphic Queries](#4-polymorphic-queries)
5. [The Classic Interview Trap: NOT NULL Constraints](#5-the-classic-interview-trap-constraints)
6. [Mapping Subclasses (@DiscriminatorValue)](#6-mapping-subclasses)
7. [Single Table vs Joined Strategy](#7-vs-joined)
8. [Single Table vs Table-per-Class](#8-vs-table-per-class)
9. [Impact on Performance](#9-performance)
10. [Polymorphism in Repositories](#10-repositories)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is JPA Inheritance?
JPA Inheritance allows you to map an object-oriented inheritance hierarchy (e.g., `Car` and `Bike` extending `Vehicle`) to a relational database.

---

## 2. Why Use Single Table?
In the **Single Table** strategy (the default), all classes in the hierarchy are mapped to a **single database table**. 
- **Pros**: Fastest performance (no joins needed), simple schema.
- **Cons**: Subclass-specific columns must be nullable.

---

## 3. The Discriminator Column
Because all objects are in one table, JPA needs a way to tell which row belongs to which class. It uses a "Discriminator Column" (e.g., `dtype` or `vehicle_type`) that stores a string or integer identifying the subclass.

---

## 4. Polymorphic Queries
When you query the base class repository (`vehicleRepository.findAll()`), Hibernate automatically performs a `SELECT` and uses the discriminator column to instantiate the correct objects (`Car`, `Bike`) in the returned list.

---

## 5. The Classic Interview Trap: Not Null
**The Trap**: You have a `Car` subclass with a `numberOfDoors` field. You want to make this column `NOT NULL` in the database.
**The Problem**: Since `Bike` rows are also in the same table, and they don't have doors, the `numberOfDoors` column **MUST** be nullable in the database. 
**The Fix**: You cannot enforce `NOT NULL` at the database level for subclass-specific fields in a Single Table strategy. You must enforce it at the application level (e.g., using Bean Validation).

---

## 6. Mapping Subclasses
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "V_TYPE")
public class Vehicle { ... }

@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle { ... }
```

---

## 7. Single Table vs Joined
- **Single Table**: 1 Table. Fast. Nullable columns.
- **Joined**: 1 Table per class. Slower (requires joins). Clean schema (supports `NOT NULL`).

---

## 8. Table-per-Class
Each concrete class has its own table containing all fields (including those from the parent). It has poor support for polymorphic queries and is rarely used.

---

## 9. Performance
Single Table is the most efficient strategy for read-heavy applications because it avoids the overhead of JOIN operations. However, as the hierarchy grows, the table can become "sparse" with many null values.

---

## 10. Repositories
You can create a repository for the base class to fetch everything, OR you can create repositories for specific subclasses if you only want to work with `Cars`.

---

## 11. Common Mistakes
1. Forgetting to define the `InheritanceType`.
2. Assuming `NOT NULL` constraints will work for subclass fields.
3. Over-using inheritance where Composition would be a better design pattern.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the default inheritance strategy?**  
A: `SINGLE_TABLE`.  
**Q: Can a base class be Abstract?**  
A: Yes, and it often should be if you don't want to instantiate generic "Vehicles".
