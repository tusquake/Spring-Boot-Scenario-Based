# JPA Cascade Types — Complete Interview Reference

## Table of Contents
1. [What is Cascading in JPA?](#1-what-is-cascading)
2. [Why Use Cascade? (Convenience vs Risk)](#2-why-use-it)
3. [The Different Cascade Types (PERSIST, REMOVE, etc.)](#3-cascade-types)
4. [CascadeType.ALL](#4-cascade-all)
5. [The Classic Interview Trap: Cascade vs OrphanRemoval](#5-the-classic-interview-trap-orphan)
6. [Cascading in One-to-One Relationships](#6-one-to-one)
7. [Cascading in One-to-Many Relationships](#7-one-to-many)
8. [Bidirectional Mapping and Cascading](#8-bidirectional)
9. [Circular Dependencies in Cascading](#9-circular)
10. [Database Level Cascade vs JPA Cascade](#10-db-vs-jpa)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Cascading?
Cascading allows you to propagate a state change from a "Parent" entity to its "Child" entities. For example, if you save a `Person`, you might want their `Address` to be saved automatically as well.

---

## 2. Why Use Cascade?
- **Convenience**: You don't have to manually call `repository.save()` for every related object.
- **Data Integrity**: Ensures that related objects are kept in sync with the parent.

---

## 3. Cascade Types
- **PERSIST**: When parent is saved, children are saved.
- **MERGE**: When parent is updated, children are updated.
- **REMOVE**: When parent is deleted, children are deleted.
- **REFRESH**: When parent is reloaded from DB, children are reloaded.
- **DETACH**: When parent is removed from the persistence context, children are too.

---

## 4. CascadeType.ALL
A shortcut that applies all the above cascade types. While convenient, it can be dangerous if you accidentally delete a parent and lose a large amount of child data.

---

## 5. The Classic Interview Trap: orphanRemoval
**The Question**: *"What is the difference between CascadeType.REMOVE and orphanRemoval=true?"*
**The Answer**:
- **CascadeType.REMOVE**: If you delete the Parent, the Child is deleted.
- **orphanRemoval=true**: If you remove a Child from the Parent's collection (`parent.getChildren().remove(child)`), the Child is deleted from the database. `CascadeType.REMOVE` would leave the child as an "orphan" in the DB.

---

## 6. One-to-One Cascading
Commonly used for "Profile" or "Address" tables that belong strictly to one owner.
`@OneToOne(cascade = CascadeType.ALL)`

---

## 7. One-to-Many Cascading
Commonly used for "OrderItems" in an "Order". When an order is deleted, its items should usually be deleted too.

---

## 8. Bidirectional Mapping
When using cascading in a bidirectional relationship, you usually apply the cascade to the "Parent" (the one with the `mappedBy` attribute) so that operations flow down to the children.

---

## 9. Circular Dependencies
Be careful when applying `CascadeType.ALL` to both sides of a relationship, as it can occasionally lead to infinite loops or unexpected state changes during complex updates.

---

## 10. Database vs JPA Cascade
- **JPA Cascade**: Handled by Hibernate code in the JVM. Works even if the DB doesn't support cascading.
- **DB Cascade**: Handled by the SQL engine (`ON DELETE CASCADE`). Safer and faster, but less flexible than JPA logic.

---

## 11. Common Mistakes
1. Using `CascadeType.ALL` on `Many-to-One` relationships (Deleting a child might accidentally delete the shared parent!).
2. Not managing both sides of a bidirectional relationship in the Java code (always use helper methods to add/remove children).
3. Forgetting that `orphanRemoval` only works for `One-to-One` and `One-to-Many`.

---

## 12. Quick-Fire Interview Q&A
**Q: Is PERSIST the same as SAVE?**  
A: Almost. `persist` is for new entities; `merge` is for re-attaching detached entities.  
**Q: What is the default CascadeType?**  
A: By default, NO cascading is applied. You must opt-in.
