# Scenario 87: JPA One-to-One Deep Dive

## Overview
A **One-to-One** relationship represents a link where one entity is associated with exactly one instance of another entity. While simple in theory, managing it in JPA requires understanding the **Owning Side**, **Inverse Side**, and performance implications of **Fetch Types**.

---

## 🛂 Analogy: The Traveler and the Passport

Imagine a traveler going through an international airport:
1.  **The Traveler (Owning Side)**: The traveler physically **holds** the passport. In the database, the `PERSON` table holds the `address_id` foreign key.
2.  **The Passport (Inverse Side)**: The passport is linked to the traveler, but it doesn't "hold" the traveler. It uses `mappedBy` in code to point back to the owner.

![JPA One-to-One Analogy](file:///C:/Users/tushar.seth/.gemini/antigravity/brain/ae414fa8-11be-4ae0-99dc-266f09219526/jpa_one_to_one_analogy_1774094501626.png)

---

## Key Concepts 🛠️

### 1. Owning vs. Inverse Side
-   **Owning Side (`Person`)**: The entity that has the `@JoinColumn`. It is responsible for updating the foreign key in the database.
-   **Inverse Side (`Address`)**: Uses `mappedBy`. It tells JPA, "Don't create a new column here; the relationship is already managed by the `address` field in the `Person` class."

### 2. Cascade Operations (`CascadeType.ALL`)
-   When you save a `Person`, the `Address` is saved **automatically**.
-   When you delete a `Person`, the `Address` is deleted **automatically**.
-   This ensures data integrity and reduces manual boilerplate code.

### 3. Fetch Strategies (`FetchType.LAZY`)
-   **EAGER (Default)**: Loading a Person immediately loads the Address. This can be slow if you only need the person's name.
-   **LAZY**: The Address is only loaded when you call `person.getAddress()`. This is best practice for performance.

### 4. The Lombok Trap ⚠️
Using `@Data` or `@ToString` on both sides of a bidirectional relationship causes an **infinite loop** (`Person -> Address -> Person...`), leading to a `StackOverflowError`.
-   **Fix**: Always use `@ToString.Exclude` on the Inverse side.

### 5. The JSON Serialization Loop 🌐
Just like Lombok, **Jackson** (the JSON library) will get stuck in a loop trying to serialize `Person -> Address -> Person`.
-   **Fix**: Use `@JsonManagedReference` on the Owning side and `@JsonBackReference` on the Inverse side. This tells Jackson to "break the chain" when converting objects to JSON.

### 6. Hibernate Proxy Serialization (Lazy Loading) 🧵
When you use `FetchType.LAZY`, JPA uses a **Proxy** object (ByteBuddy). By default, Jackson doesn't know how to handle this "fake" object.
-   **Error**: `Type definition error: ... ByteBuddyInterceptor`
-   **Fix**: Add `@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})` to your entity classes. This tells Jackson to ignore the internal Hibernate plumbing used for lazy loading.

---

## Testing the Scenario
Use these `curl` commands to verify the behavior:

1. **Create Person & Address (Cascade Test)**:
   ```bash
   curl -X POST http://localhost:8080/debug-application/api/scenario87/person \
   -H "Content-Type: application/json" \
   -d '{"name": "Tushar", "email": "tushar@example.com", "address": {"street": "123 Spring St", "city": "TechCity", "zipCode": "10001"}}'
   ```
   *Check console: You will see INSERT statements for BOTH tables.*

2. **Fetch Person (Lazy Test)**:
   ```bash
   curl http://localhost:8080/debug-application/api/scenario87/person/1
   ```
   *Check console: You will see the Person SQL first, and then the Address SQL only when the service accesses the street name.*

3. **Delete Person (Cascade Delete Test)**:
   ```bash
   curl -X DELETE http://localhost:8080/debug-application/api/scenario87/person/1
   ```
   *Check console: Both records are purged from the DB.*
