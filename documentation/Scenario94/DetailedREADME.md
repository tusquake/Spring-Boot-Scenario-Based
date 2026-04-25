# JPA ID Generation & Batching — Complete Interview Reference

## Table of Contents
1. [Primary Key Generation Strategies](#1-strategies)
2. [GenerationType.IDENTITY](#2-identity)
3. [GenerationType.SEQUENCE](#3-sequence)
4. [GenerationType.TABLE](#4-table)
5. [GenerationType.AUTO](#5-auto)
6. [The Classic Interview Trap: IDENTITY vs JDBC Batching](#6-the-classic-interview-trap-batching)
7. [Why SEQUENCE is faster for Bulk Inserts](#7-why-sequence-faster)
8. [UUID Generation (App-side vs DB-side)](#8-uuid-generation)
9. [Configuring hibernate.jdbc.batch_size](#9-batch-config)
10. [Allocation Size & Hi-Lo Algorithm](#10-allocation-size)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Generation Strategies
JPA provides four standard ways to generate primary keys automatically. Choosing the right one is critical for both database portability and performance.

---

## 2. GenerationType.IDENTITY
- **How it works**: Uses the database's auto-increment feature (e.g., `SERIAL` in Postgres, `AUTO_INCREMENT` in MySQL).
- **Behavior**: The database generates the ID after the row is inserted.

---

## 3. GenerationType.SEQUENCE
- **How it works**: Uses a database sequence object to fetch IDs before the insert happens.
- **Behavior**: Hibernate can request multiple IDs in a single call to the database.

---

## 4. GenerationType.TABLE
- **How it works**: Uses a separate database table to simulate a sequence. 
- **Behavior**: Extremely slow because it requires locking the table for every ID generation. **Avoid using this** unless your database supports nothing else.

---

## 5. GenerationType.AUTO
The default strategy. Hibernate looks at your database dialect and chooses the "best" available strategy (usually SEQUENCE or IDENTITY).

---

## 6. The Classic Interview Trap: Batching
**The Question**: *"Why does JDBC batching fail when using GenerationType.IDENTITY?"*
**The Answer**: Hibernate's "Transactional Write-behind" strategy requires every entity in the persistence context to have an ID. With `IDENTITY`, the ID is only known **after** the insert. Therefore, Hibernate must execute each `INSERT` immediately to get the ID, which makes batching impossible.

---

## 7. Sequence is Faster for Bulk
With `SEQUENCE`, Hibernate can fetch 50 IDs at once (using `allocationSize = 50`) and then send 50 `INSERT` statements to the database in a single network roundtrip (Batching). This can be 10x-100x faster than `IDENTITY` for bulk uploads.

---

## 8. UUID Generation
- **Pros**: IDs can be generated in the application layer without any database roundtrip. Perfect for distributed systems.
- **Cons**: Large (128-bit) keys can lead to larger indexes and slightly slower performance compared to integers.

---

## 9. Configuring Batch Size
Even if you use `SEQUENCE`, you must explicitly enable batching in your properties:
`spring.jpa.properties.hibernate.jdbc.batch_size=20`
`spring.jpa.properties.hibernate.order_inserts=true`

---

## 10. Allocation Size
The `allocationSize` in `@SequenceGenerator` should match the "increment by" value in your database sequence. If they don't match, you will get duplicate key errors.

---

## 11. Common Mistakes
1. Using `IDENTITY` for high-volume data ingestion.
2. Not setting `order_inserts` and `order_updates`, which can break batching if different entity types are interleaved.
3. Using `TABLE` strategy in production.

---

## 12. Quick-Fire Interview Q&A
**Q: Which strategy is best for MySQL?**  
A: MySQL doesn't support Sequences (until recently), so `IDENTITY` is the standard.  
**Q: Which strategy is best for PostgreSQL?**  
A: `SEQUENCE`. It supports batching and is highly optimized.
