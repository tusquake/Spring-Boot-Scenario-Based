## Overview
**ABAC (Attribute-Based Access Control)** is a fine-grained security model that evaluates permissions based on the **attributes** of the Subject (User), the Resource (Data), and the Action (READ/WRITE).

---

## 🏦 Real-World Analogy: The Bank Account

Imagine a bank vault with many safe deposit boxes:
-   **RBAC (Role-Based)**: If you have a 'STAFF' key, you can open any box.
-   **ABAC (Attribute-Based)**: Even if you are a customer, you can only open a box if **YOUR NAME** matches the **OWNER NAME** on the box.

![ABAC Security Analogy](file:///C:/Users/tushar.seth/.gemini/antigravity/brain/ae414fa8-11be-4ae0-99dc-266f09219526/abac_security_analogy_v2_1774083042972.png)

---

## Key Components in Spring 🛠️

### 1. Custom PermissionEvaluator
This is the "Security Guard" that checks the attributes. We implement the `hasPermission` method to compare the authenticated user's name with the `BankAccount.getOwner()`.

### 2. @PreAuthorize SpEL
We use Spring Expression Language to call this guard dynamically:
```java
@PreAuthorize("hasPermission(#id, 'BankAccount', 'READ')")
```

---

## Technical Comparison 💡

| Concept | Question Asked | Logic Type |
| :--- | :--- | :--- |
| **RBAC** | Is user an ADMIN? | Static (Roles) |
| **ABAC** | Does client X own account Y? | Dynamic (Attributes) |

---

## Testing the Scenario
1. **Create a Document**:
   `POST /api/scenario82/documents`.
   Body: `{"name": "Secret Plans", "content": "Internal launch data"}`.
   - Note: The controller automatically sets you as the owner.

2. **View as Owner**:
   `GET /api/scenario83/documents/1`.
   - **Result**: `200 OK`.

3. **View as Someone Else**:
   - (Simulation): If another user tried to access ID 1, they would receive a **403 Forbidden** error, even if they have the same `ROLE_USER`.
