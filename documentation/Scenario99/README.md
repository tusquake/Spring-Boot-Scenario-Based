# Scenario 99: JPA Composite Keys (@EmbeddedId)

## Overview
What do you do when a table has **two or more columns** that together form the Primary Key? 

In JPA, we handle this by creating a separate "ID Class" and embedding it into the main Entity.

---

## 🏗️ Key Components

### 1. The `@Embeddable` ID Class
The `Scenario99OrderItemId` class represents the composite key.

**Requirements for a Composite Key**:
1.  **`Serializable`**: Hibernate often needs to serialize IDs for caching or session management.
2.  **`equals()` and `hashCode()`**: Essential so JPA can compare IDs to see if two objects represent the same database row.
3.  **No-args Constructor**: Required for Hibernate to instantiate the class.

### 2. The `@EmbeddedId` Annotation
In the main entity (`Scenario99OrderItem`), we don't use `@Id`. We use `@EmbeddedId` to point to our key object.

```java
@EmbeddedId
Scenario99OrderItemId id;
```

---

## 🧼 Clean Code with `@FieldDefaults`
In this scenario, we've also used **Lombok's `@FieldDefaults(level = AccessLevel.PRIVATE)`**.
*   This makes every field `private` by default.
*   It removes the repetitive `private` keyword from your code, making it much more readable.

---

## 🧪 Testing

### 1. Create an Item with a Composite Key
We must provide both `orderId` and `productId`.
```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario99/create?orderId=101&productId=505&quantity=2"
```

### 2. Fetch the Item using the Composite Key
Notice that to find the item, we still have to provide both parts of the key.
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario99/find?orderId=101&productId=505"
```

---

## Interview Tip 💡
**Q**: *"What is the difference between @EmbeddedId and @IdClass?"*  
**A**: 
*   **`@EmbeddedId`**: The ID is a single object in your Entity. It's more Object-Oriented and allows you to pass the whole ID object around easily.
*   **`@IdClass`**: The fields of the ID are flattened into the Entity class itself. It's often used when you want the entity to "look" like a flat table but still have a composite key.
