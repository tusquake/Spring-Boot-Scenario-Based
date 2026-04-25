# JPA Soft Deletes — Complete Interview Reference

## Table of Contents
1. [What is Soft Delete?](#1-what-is-soft-delete)
2. [Hard Delete vs Soft Delete](#2-hard-vs-soft)
3. [@SQLDelete Annotation](#3-sqldelete)
4. [@SQLRestriction (@Where) Annotation](#4-sqlrestriction)
5. [The Classic Interview Trap: Soft Delete vs Unique Constraints](#5-the-classic-interview-trap-unique-constraints)
6. [Fetching Deleted Records](#6-fetching-deleted)
7. [Soft Delete in Relationships (Cascading)](#7-relationships)
8. [Auditing Soft Deletes (Deleted By/At)](#8-auditing)
9. [Restoring Deleted Records](#9-restoring)
10. [Performance Impact of Soft Deletes](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Soft Delete?
Soft delete is the practice of marking a record as "deleted" (usually via a boolean flag) instead of physically removing it from the database table.

---

## 2. Hard Delete vs Soft Delete
- **Hard Delete**: `DELETE FROM table WHERE id = ?`. Data is gone forever.
- **Soft Delete**: `UPDATE table SET deleted = true WHERE id = ?`. Data remains for auditing/recovery.

---

## 3. @SQLDelete Annotation
A Hibernate annotation that allows you to override the default `DELETE` statement.
```java
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id = ?")
```

---

## 4. @SQLRestriction (@Where) Annotation
Used to filter out deleted records from all queries automatically.
```java
@SQLRestriction("deleted = false")
```

---

## 5. The Classic Interview Trap: Soft Delete vs Unique Constraints
**The Trap**: If you have a unique constraint on `email`, and you soft-delete a user, a new user cannot sign up with the same email because the "deleted" record still occupies that unique slot.
**The Fix**: Include the `deleted_at` timestamp in your unique constraint or use a composite key.

---

## 6. Fetching Deleted Records
To fetch deleted records, you typically need to use a **Native Query** or a separate entity/repository that doesn't have the `@SQLRestriction` filter applied.

---

## 7. Soft Delete in Relationships (Cascading)
Be careful! If you soft-delete a Parent, the Children might still be "active" unless you also trigger a soft-delete on them. Hibernate's `@SQLDelete` does not automatically cascade the *custom* SQL to children.

---

## 8. Auditing Soft Deletes (Deleted By/At)
Always combine soft deletes with auditing fields like `deleted_at` and `deleted_by` to know exactly when and who performed the action.

---

## 9. Restoring Deleted Records
Restoring is as simple as setting `deleted = false`. This is a huge advantage of soft deletes for customer support scenarios.

---

## 10. Performance Impact of Soft Deletes
Soft deletes can lead to large tables and "fragmented" indexes. Over time, you may need a background job to physically purge (Hard Delete) records that have been soft-deleted for more than X years.

---

## 11. Common Mistakes
1. Forgetting to add an index on the `deleted` column.
2. Assuming `repository.count()` includes deleted records (it doesn't if `@SQLRestriction` is used).

---

## 12. Quick-Fire Interview Q&A
**Q: Does @SQLDelete work with CrudRepository.deleteById()?**  
A: Yes, Hibernate intercepts the call and runs your custom SQL instead.  
**Q: How do you bypass @SQLRestriction?**  
A: By using a native SQL query or opening a new Hibernate Session/Filter.
