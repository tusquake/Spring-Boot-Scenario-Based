# JPA Composite Keys — Complete Interview Reference

## Table of Contents
1. [What is a Composite Key?](#1-definition)
2. [When to Use Composite Keys?](#2-when-to-use)
3. [The Two Approaches: @EmbeddedId vs @IdClass](#3-two-approaches)
4. [Implementing @EmbeddedId](#4-embedded-id)
5. [Implementing @IdClass](#5-id-class)
6. [The Classic Interview Trap: Implementing Equals and HashCode](#6-the-classic-interview-trap-equals)
7. [Composite Keys in Relationships (@MapsId)](#7-maps-id)
8. [Finding by Composite Key in Repositories](#8-repositories)
9. [Primary Key constraints in the Database](#9-database)
10. [Performance of Composite Keys](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Composite Key?
A composite key is a primary key that consists of more than one column. It is used when a single column is not enough to uniquely identify a record in a table.

---

## 2. When to Use?
- **Join Tables**: In a many-to-many relationship, the join table usually has a composite key made of the two related IDs.
- **Natural Keys**: When the combination of two "real-world" fields (like `order_id` and `product_id`) is naturally unique.

---

## 3. Two Approaches
- **@EmbeddedId**: (Recommended) You create a separate "ID Class" and embed it into your entity as a single field. It's more object-oriented.
- **@IdClass**: You define the ID fields directly in your entity and point to an "ID Class" that matches those fields.

---

## 4. Implementing @EmbeddedId
The ID class must:
1. Be marked with `@Embeddable`.
2. Implement `Serializable`.
3. Override `equals()` and `hashCode()`.

---

## 5. The Classic Interview Trap: Equals & HashCode
**The Trap**: You implement a composite key but don't override `hashCode()`.
**The Problem**: JPA uses the ID to identify objects in the "Persistence Context" (First-level cache). If two ID objects with the same values produce different hashcodes, JPA will treat them as different entities, leading to duplicate records or "Entity not found" errors during updates.
**The Fix**: Always use your IDE or Lombok to generate robust `equals` and `hashCode` methods based on all fields of the composite key.

---

## 6. @MapsId
If one of the parts of your composite key is also a foreign key to another entity, you use `@MapsId`. This tells JPA that the ID value should be copied from the related entity.

---

## 7. Repositories
When using a composite key, your repository definition looks like this:
`public interface MyRepo extends JpaRepository<MyEntity, MyIdClass> { ... }`
You must pass an instance of `MyIdClass` to `findById()`.

---

## 8. Database Schema
In the database, the primary key will be defined as `PRIMARY KEY (order_id, product_id)`. This automatically creates a composite index, making searches by the full key very fast.

---

## 9. ID Class vs Entity
Remember that the ID class is just a data carrier. It should not contain any business logic or JPA relationships; it should only contain the basic fields that make up the key.

---

## 10. Performance
Composite keys are slightly slower than simple auto-incrementing integers because the index is larger and the comparison logic is more complex. However, for join tables, they are the most space-efficient choice.

---

## 11. Common Mistakes
1. Forgetting to implement `Serializable` in the ID class.
2. Not overriding `equals` and `hashCode`.
3. Using `@GeneratedValue` inside a composite key (JPA does not support auto-generating parts of a composite key).

---

## 12. Quick-Fire Interview Q&A
**Q: Which approach is better, @EmbeddedId or @IdClass?**  
A: `@EmbeddedId` is generally preferred because it is more explicit and easier to pass around as a single object.  
**Q: Can a composite key have 3 or more columns?**  
A: Yes, there is no technical limit to the number of columns in a composite key.
