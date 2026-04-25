# JPA Auditing (@CreatedDate, @LastModifiedDate) — Complete Interview Reference

## Table of Contents
1. [What is JPA Auditing?](#1-what-is-jpa-auditing)
2. [Enabling Auditing (@EnableJpaAuditing)](#2-enabling-auditing)
3. [The Core Annotations (@CreatedDate, @CreatedBy, etc.)](#3-core-annotations)
4. [Auditing Entity Listeners (AuditingEntityListener.class)](#4-entity-listeners)
5. [The Classic Interview Trap: Missing the AuditorAware Bean](#5-the-classic-interview-trap-auditoraware)
6. [Using @MappedSuperclass for Auditing Fields](#6-mapped-superclass)
7. [Temporal Types (Date vs LocalDateTime vs ZonedDateTime)](#7-temporal-types)
8. [Auditing in Distributed Systems](#8-distributed-systems)
9. [Manual Auditing vs Automated Auditing](#9-manual-vs-automated)
10. [Auditing with Spring Security Integration](#10-security-integration)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is JPA Auditing?
JPA Auditing is a feature that automatically populates fields like "created_at", "updated_at", "created_by", and "updated_by" without you having to manually set them in your service logic.

---

## 2. Enabling Auditing
You must add `@EnableJpaAuditing` to one of your configuration classes (usually the main Application class) to activate the auditing lifecycle.

---

## 3. The Core Annotations
- `@CreatedDate`: Sets the timestamp when the entity is first saved.
- `@LastModifiedDate`: Updates the timestamp every time the entity is updated.
- `@CreatedBy`: Captures the username of the user who created the record.
- `@LastModifiedBy`: Captures the username of the user who last updated the record.

---

## 4. Auditing Entity Listeners
You must add `@EntityListeners(AuditingEntityListener.class)` to your entity or base class. This tells JPA to intercept the "PrePersist" and "PreUpdate" events to populate the auditing fields.

---

## 5. The Classic Interview Trap: Missing AuditorAware
**The Trap**: You added `@CreatedBy`, but the field is always `null`.
**The Fix**: For "CreatedBy" to work, Spring needs to know *who* the current user is. You must provide a bean that implements `AuditorAware<String>`, which typically fetches the username from the `SecurityContextHolder`.

---

## 6. Using @MappedSuperclass
Instead of adding auditing fields to every entity, create a base class (e.g., `BaseEntity`) marked with `@MappedSuperclass` and have all your entities extend it.

---

## 7. Temporal Types
Modern Spring apps should use `java.time.LocalDateTime` or `java.time.Instant`. Avoid `java.util.Date` as it is legacy and not thread-safe.

---

## 8. Auditing in Distributed Systems
In microservices, ensure all servers are synchronized with NTP (Network Time Protocol) so that timestamps remain consistent across the system.

---

## 9. Manual vs Automated Auditing
Automated auditing is cleaner, but manual auditing (setting fields in code) might be necessary if you have complex logic (e.g., "created_by" should be a specific system ID rather than the logged-in user).

---

## 10. Spring Security Integration
JPA Auditing is tightly integrated with Spring Security. The `AuditorAware` implementation is the bridge that connects the two.

---

## 11. Common Mistakes
1. Forgetting `@EnableJpaAuditing`.
2. Forgetting `@EntityListeners(AuditingEntityListener.class)`.
3. Using `@CreatedDate` on a field that isn't a supported temporal type.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I change the 'createdDate' after it's set?**  
A: By default, no. You should mark it as `updatable = false` in the `@Column` annotation.  
**Q: Does auditing work for native SQL queries?**  
A: No. Auditing is a JPA lifecycle feature. Native queries bypass this lifecycle.
