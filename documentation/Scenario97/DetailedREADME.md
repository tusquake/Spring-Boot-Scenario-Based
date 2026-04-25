# JPA Embeddables — Complete Interview Reference

## Table of Contents
1. [What are Embeddables?](#1-what-are-embeddables)
2. [Embeddable vs Entity](#2-embeddable-vs-entity)
3. [Using @Embeddable and @Embedded](#3-annotations)
4. [Reusability of Value Objects](#4-reusability)
5. [The Classic Interview Trap: Multiple Embeddings of the same type](#5-the-classic-interview-trap-overrides)
6. [Attribute Overrides (@AttributeOverride)](#6-attribute-overrides)
7. [Null Handling in Embeddables](#7-null-handling)
8. [Nesting Embeddables](#8-nesting)
9. [Embeddables inside Collections (@ElementCollection)](#9-collections)
10. [Validation in Embeddables](#10-validation)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What are Embeddables?
An Embeddable class represents a set of fields that are logically grouped together but do not have their own identity (no `@Id`). Instead, they are "embedded" into a parent entity and stored as columns in the parent entity's table.

---

## 2. Embeddable vs Entity
- **Entity**: Has its own ID and lifecycle. It exists in its own table.
- **Embeddable**: Does NOT have an ID. Its lifecycle is tied to the parent entity. It "flattens" its fields into the parent's table.

---

## 3. Annotations
- `@Embeddable`: Marked on the class that will be embedded (e.g., `Address`).
- `@Embedded`: Marked on the field in the parent entity that holds the embeddable.

---

## 4. Reusability
The primary benefit is reusability. You can define an `Address` class once and use it in `User`, `Company`, `Warehouse`, and `Store` entities.

---

## 5. The Classic Interview Trap: Duplicate Fields
**The Trap**: You want to have two addresses in one entity: `homeAddress` and `workAddress`, both of type `Address`.
**The Problem**: JPA will try to create two columns named `street`, `city`, etc., which will cause a database naming conflict.
**The Fix**: Use `@AttributeOverrides` to rename the columns for each specific embedding.

---

## 6. Attribute Overrides
```java
@Embedded
@AttributeOverrides({
    @AttributeOverride(name = "street", column = @Column(name = "work_street")),
    @AttributeOverride(name = "city", column = @Column(name = "work_city"))
})
private Address workAddress;
```

---

## 7. Null Handling
If all fields of an embedded object are `null` in the database, Hibernate will return `null` for the entire embedded field when fetching the entity. It does not instantiate an object with all-null fields.

---

## 8. Nesting
Embeddables can be nested. An `Address` can contain a `Coordinates` embeddable, which is then embedded into a `User`. All fields will still be flattened into the single `User` table.

---

## 9. ElementCollection
You can store a list of embeddables using `@ElementCollection`. JPA will create a separate collection table, but the items in that table will not have their own identity—they are strictly tied to the parent ID.

---

## 10. Validation
You can add JSR-303 validation annotations (like `@NotBlank`) to the fields of an `@Embeddable`. When you validate the parent entity, Spring will automatically validate the embedded fields as well.

---

## 11. Common Mistakes
1. Forgetting `@Embeddable` on the class.
2. Trying to add a `@ManyToOne` relationship inside an `@Embeddable` (It is supported but can become complex; prefer keeping embeddables as simple data holders).
3. Not using `@AttributeOverrides` when having multiple fields of the same embeddable type.

---

## 12. Quick-Fire Interview Q&A
**Q: Does an Embeddable have its own repository?**  
A: No. You always interact with them through the parent entity's repository.  
**Q: Can an Embeddable be shared between two entities?**  
A: Yes, as a class definition, but once instantiated and saved, the data belongs to only one specific parent row.
