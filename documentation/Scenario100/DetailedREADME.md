# JPA JSON Columns — Complete Interview Reference

## Table of Contents
1. [Introduction to JSON in Relational DBs](#1-introduction)
2. [JSON vs JSONB (PostgreSQL)](#2-json-vs-jsonb)
3. [Mapping JSON to Java Objects](#3-mapping)
4. [Using Hypersistence Utils (vladmihalcea)](#4-hypersistence-utils)
5. [The Classic Interview Trap: Querying inside JSON](#5-the-classic-interview-trap-querying)
6. [Updating JSON Fields (Atomic Updates)](#6-updating)
7. [Indexing JSON Columns](#7-indexing)
8. [Validation of JSON Content](#8-validation)
9. [Schema-less vs Structured Data](#9-schema-less)
10. [Performance Impact](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Modern relational databases like PostgreSQL and MySQL support storing semi-structured data in JSON columns. This allows you to combine the power of ACID transactions with the flexibility of a NoSQL-like schema.

---

## 2. JSON vs JSONB
- **JSON**: Stores the raw string. Fast to write, slow to read (must be parsed every time).
- **JSONB**: (PostgreSQL specific) Stores the data in a decomposed binary format. Slower to write, but **much faster** to read and supports indexing.

---

## 3. Mapping in JPA
JPA (standard) does not have a native `@JsonColumn` annotation. You traditionally had to use an `AttributeConverter` to convert between a Java object and a String.

---

## 4. Hypersistence Utils
Most Spring developers use the **Hypersistence Utils** library by Vlad Mihalcea. It provides a `@Type` annotation that handles the complex logic of mapping Java objects/Maps to native DB JSON types automatically.

---

## 5. The Classic Interview Trap: Searching JSON
**The Trap**: You store a "Tags" list inside a JSON column. Now you want to find all rows where `tags` contains "PREMIUM".
**The Problem**: Standard JPQL (`WHERE metadata.tags LIKE '%PREMIUM%'`) is extremely slow because it performs a full table scan and string matching.
**The Fix**: You must use **Native Queries** with database-specific operators (e.g., the `@>` operator in Postgres) to leverage JSON indexes (GIN indexes).

---

## 6. Updating JSON Fields
One downside of JSON columns is that you usually have to fetch the whole object, modify one field in Java, and then save the whole object back to the DB. This can lead to "Lost Updates" if two threads try to modify different parts of the same JSON object simultaneously.

---

## 7. Indexing
In PostgreSQL, you can create a **GIN (Generalized Inverted Index)** on a JSONB column. This allows the database to instantly find keys or values nested deep inside the JSON structure.

---

## 8. Validation
You can use standard JSR-303 annotations on your Java "Metadata" classes. When you save the parent entity, the JSON object will be validated before it is serialized to the database.

---

## 9. Schema-less vs Structured
**Rule of thumb**: Use columns for mandatory data that you query frequently. Use JSON for optional, highly variable data, or "blob" data that you only need for display purposes (like front-end UI state).

---

## 10. Performance
JSON columns add a slight overhead for serialization (Jackson). However, they can significantly simplify your schema by avoiding dozens of "one-to-many" tables for simple key-value pairs.

---

## 11. Common Mistakes
1. Storing primary identifiers inside JSON (IDs should always be proper columns).
2. Forgetting to register the JSON type in your Hibernate dialect.
3. Using JSON for data that is perfectly structured and would be better in a normal table.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use JPQL with JSON columns?**  
A: No, standard JPQL does not understand JSON paths. You must use Native Queries or the `Criteria API` with specific extensions.  
**Q: What happens if the JSON in the DB is malformed?**  
A: Jackson will throw a `JsonParseException` when Hibernate tries to load the entity.
