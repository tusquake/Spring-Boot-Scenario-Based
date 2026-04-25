# JPA Auditing — Complete Interview Reference

## Table of Contents
1. [What is JPA Auditing?](#1-what-is-jpa-auditing)
2. [Why Use Auditing? (Traceability)](#2-why-use-it)
3. [Enabling Auditing (@EnableJpaAuditing)](#3-enabling)
4. [Auditing Annotations (@CreatedDate, etc.)](#4-annotations)
5. [The Classic Interview Trap: Missing AuditorAware](#5-the-classic-interview-trap-auditor)
6. [Auditing in MappedSuperclass](#6-mapped-superclass)
7. [DateTimeProvider for Timezones](#7-datetime-provider)
8. [Auditing with Hibernate Envers (Versioning)](#8-envers)
9. [Manual vs Automatic Auditing](#9-manual-vs-auto)
10. [Performance Impact of Auditing](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is JPA Auditing?
JPA Auditing is a feature that automatically populates entity fields like `created_date`, `last_modified_date`, `created_by`, and `last_modified_by` whenever an entity is saved or updated.

---

## 2. Why Use Auditing?
- **Security**: Know who modified a sensitive record.
- **Debugging**: See when a record was created to correlate with logs.
- **Compliance**: Many industries (Finance, Health) require a full audit trail of all data changes.

---

## 3. Enabling Auditing
You must add `@EnableJpaAuditing` to a configuration class. Without this, the annotations on your entities will be ignored.

---

## 4. Key Annotations
- `@CreatedDate`: Automatically sets the timestamp when the entity is first persisted.
- `@LastModifiedDate`: Updates the timestamp every time the entity is modified.
- `@CreatedBy`: Captures the username of the person who created the record.
- `@LastModifiedBy`: Captures the username of the last person to edit the record.

---

## 5. The Classic Interview Trap: @CreatedBy
**The Trap**: You add `@CreatedBy` to your entity, but it remains `null` even after saving.
**The Problem**: Spring Data JPA doesn't know who the "current user" is. It doesn't automatically talk to Spring Security.
**The Fix**: You must implement the `AuditorAware<String>` interface and return the current user's name from the `SecurityContextHolder`.

---

## 6. MappedSuperclass
Instead of adding auditing fields to every entity, it is a best practice to create a `BaseEntity` marked with `@MappedSuperclass` and `@EntityListeners(AuditingEntityListener.class)`. All your entities can then extend this base class.

---

## 7. DateTimeProvider
By default, Spring uses the system time. If your application needs to support multiple timezones, you can provide a custom `DateTimeProvider` bean to ensure all audit timestamps are in **UTC**.

---

## 8. Auditing vs Versioning
- **Auditing**: Records *who* and *when*.
- **Versioning (Envers)**: Records the *actual data* that changed. It creates a "shadow" table (e.g., `product_AUD`) that stores a snapshot of every version of the entity.

---

## 9. Manual vs Automatic
While you *could* set these fields manually in your service layer, automatic auditing is cleaner, less error-prone, and ensures that the timestamps are consistent across the entire application.

---

## 10. Performance
Auditing adds a very tiny overhead to save operations (just fetching the time and user). It is usually negligible compared to the database I/O itself.

---

## 11. Common Mistakes
1. Forgetting `@EnableJpaAuditing`.
2. Not adding `@EntityListeners(AuditingEntityListener.class)` to the entity.
3. Using `java.util.Date` instead of modern Java 8 `LocalDateTime` or `Instant`.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @CreatedDate on a non-JPA entity?**  
A: No, these are specific to Spring Data JPA's lifecycle events.  
**Q: How do I audit "Soft Deletes"?**  
A: You can use a `@PreRemove` lifecycle hook to set a `deleted_at` timestamp instead of actually deleting the row.
