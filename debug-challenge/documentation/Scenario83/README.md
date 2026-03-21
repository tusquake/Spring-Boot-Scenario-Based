# Scenario 83: ABAC Security (Attribute-Based Access Control)

## Overview
Standard **RBAC (Role-Based Access Control)** answers the question: *"Does the user have the ADMIN role?"*
**ABAC (Attribute-Based Access Control)** answers the question: *"Does the user have permission to access THIS specific resource?"*

## Key Components 🔐

### 1. Custom PermissionEvaluator
Spring Security provides the `PermissionEvaluator` interface. We implemented it to check the `ownerUsername` attribute of a Document against the current `Principal`.

### 2. @PreAuthorize("hasPermission(...)")
This is a powerful SpEL (Spring Expression Language) command.
```java
@PreAuthorize("hasPermission(#id, 'Scenario83Document', 'READ')")
```
- It intercepts the method call.
- It passes the `id` and the type `Scenario83Document` to our `CustomPermissionEvaluator`.
- If the evaluator returns `false`, Spring throws an `AccessDeniedException` (403 Forbidden).

### 3. @EnableMethodSecurity
This annotation (usually in a config class) activates the use of `@PreAuthorize` and `@PostAuthorize` throughout the application.

---

## RBAC vs ABAC: The Interview Difference 💡

| Feature | RBAC | ABAC |
| :--- | :--- | :--- |
| **Check** | Is user an ADMIN/USER? | Does user own THIS record? |
| **Logic** | Static (Roles) | Dynamic (Attributes) |
| **Complexity** | Low | Medium/High |
| **Scalability** | Hard to manage 1000s of roles. | Easy to manage via policies. |

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
