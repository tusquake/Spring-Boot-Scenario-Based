# Hibernate Envers: Entity Versioning — Complete Interview Reference

## Table of Contents
1. [What is Hibernate Envers?](#1-what-is-envers)
2. [Versioning vs Auditing (@Audited vs @CreatedDate)](#2-versioning-vs-auditing)
3. [The _AUD Tables (The Audit Schema)](#3-audit-tables)
4. [The REVINFO Table](#4-revinfo-table)
5. [The Classic Interview Trap: Performance & Storage](#5-the-classic-interview-trap-performance)
6. [Querying History with AuditReader](#6-querying-history)
7. [Revision Types (ADD, MOD, DEL)](#7-revision-types)
8. [Customizing Revision Entities (@RevisionEntity)](#8-custom-revision)
9. [Conditional Auditing (@AuditOverride)](#9-conditional-auditing)
10. [Restoring Old Versions](#10-restoring)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Hibernate Envers?
Hibernate Envers is a module that provides historical versioning of your entities. It tracks every change (INSERT, UPDATE, DELETE) made to an entity and stores the history in dedicated audit tables.

---

## 2. Versioning vs Auditing
- **Auditing (@CreatedDate)**: Only stores the *last* modified time and the creator. You lose the history of intermediate changes.
- **Versioning (Envers)**: Stores every single version of the record. You can see what the user's name was 3 years ago vs today.

---

## 3. The _AUD Tables
For every table you audit (e.g., `customers`), Envers creates a corresponding `customers_aud` table. This table contains all the columns of the original table plus two metadata columns:
- `REV`: The revision ID.
- `REVTYPE`: The type of operation (0=ADD, 1=MOD, 2=DEL).

---

## 4. The REVINFO Table
Envers uses a global `REVINFO` table to store metadata for every transaction. It contains the `REV` ID and a `REVTSTMP` (timestamp). All entities modified in the same transaction will share the same revision ID.

---

## 5. The Classic Interview Trap: Performance
**The Trap**: Auditing everything in a high-write system.
**The Problem**: Every `INSERT` now becomes two inserts (one to the main table, one to the `_AUD` table). Every `UPDATE` also becomes an insert to the audit table. This doubles your write latency and significantly increases database storage requirements.
**The Fix**: Only audit business-critical tables (e.g., Orders, Users, Payments) and exclude non-critical fields using `@NotAudited`.

---

## 6. Querying History with AuditReader
You can use the `AuditReader` API to travel back in time:
```java
AuditReader reader = AuditReaderFactory.get(entityManager);
Customer oldCustomer = reader.find(Customer.class, customerId, revisionId);
```

---

## 7. Revision Types
- **ADD (0)**: The record was created.
- **MOD (1)**: One or more fields were updated.
- **DEL (2)**: The record was deleted (the audit table stores the last state before deletion).

---

## 8. Customizing Revision Entities
By default, Envers only stores the timestamp. You can create a custom `@RevisionEntity` to also store the `username`, `IP address`, or `User-Agent` for every change.

---

## 9. Conditional Auditing
You can use `@Audited` at the class level to audit everything, or at the field level to audit only specific columns. Use `@NotAudited` on sensitive fields like `password`.

---

## 10. Restoring Old Versions
Envers makes it easy to implement an "Undo" feature. You can fetch the data from a previous revision and save it back as the current state of the entity.

---

## 11. Common Mistakes
1. Auditing large BLOB/CLOB columns (wastes massive amounts of space).
2. Forgetting to index the `REV` column in the audit tables.
3. Not realizing that Envers requires a transaction to be active to create a revision.

---

## 12. Quick-Fire Interview Q&A
**Q: Can Envers audit relationships?**  
A: Yes, it can audit both `@OneToMany` and `@ManyToMany` relationships, creating join-audit tables.  
**Q: Does Envers support soft deletes?**  
A: Yes, it will record the deletion as a `REVTYPE=2` in the audit table.
