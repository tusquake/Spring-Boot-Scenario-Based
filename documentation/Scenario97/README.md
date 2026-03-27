# Scenario 97: JPA Embeddables (@Embeddable and @Embedded)

## Overview
How do you group related fields into a reusable Java object while keeping everything in a single database table?

Standard Object-Oriented design suggests we should have an `Address` class, but standard Database design often suggests a single `users` table with address columns. JPA **Embeddables** provide the bridge between these two worlds.

---

## 🏗️ Key Components

### 1. The `@Embeddable` Class
The `Scenario97Address` class is marked as `@Embeddable`. This tells JPA that it doesn't have its own identity (no `@Id`) and will be "pasted" into any entity that embeds it.

### 2. The `@Embedded` Field
In `Scenario97User`, we Use `@Embedded` to include the address:

```java
@Embedded
private Scenario97Address homeAddress;
```

### 3. `@AttributeOverrides` (The Power Feature)
What if a User has **two** addresses? By default, JPA would try to map both to the same column names (`street`, `city`), causing a conflict. We use `@AttributeOverrides` to differentiate them:

```java
@Embedded
@AttributeOverrides({
    @AttributeOverride(name = "street", column = @Column(name = "work_street")),
    @AttributeOverride(name = "city", column = @Column(name = "work_city"))
})
private Scenario97Address workAddress;
```

---

## 🆚 Embeddable vs. @OneToOne

| Feature | @Embedded | @OneToOne |
| :--- | :--- | :--- |
| **Tables** | Single Table (same as parent). | Two separate tables. |
| **Identity** | No separate ID (Value Object). | Has its own ID and Lifecycle. |
| **Performance** | Very Fast (No Joins). | Requires Joins or extra Selects. |
| **Querying** | Easy (`user.address.city`). | Requires Joins. |

---

## 🧪 Testing

### 1. Seed a User with Dual Addresses
```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario97/seed"
```

### 2. Verify Result
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario97/users"
```

### 3. Test Null Handling
What happens if you save a user with `workAddress = null`? Hibernate will set all `work_*` columns in that row to `NULL`.
```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario97/create-no-work?name=GhostUser"
```

---

## Interview Tip 💡
**Q**: *"When should you use @Embedded instead of @OneToOne?"*  
**A**: *"Use `@Embedded` when the sub-object has no independent existence and its lifecycle is strictly bound to the parent. For example, an `Address` belongs to a `User`. If the User is deleted, the address record (the columns) naturally vanishes. If you need the sub-object to be shared between multiple entities or have its own ID, use `@OneToOne` or `@ManyToOne` instead."*
