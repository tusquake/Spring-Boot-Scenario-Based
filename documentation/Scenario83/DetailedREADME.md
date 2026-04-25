# ABAC & PermissionEvaluator — Complete Interview Reference

## Table of Contents
1. [RBAC vs ABAC](#1-rbac-vs-abac)
2. [What is a PermissionEvaluator?](#2-permission-evaluator)
3. [Implementing Custom Permission Logic](#3-implementing-logic)
4. [The hasPermission() Expression](#4-haspermission-expression)
5. [The Classic Interview Trap: Performance of ABAC](#5-the-classic-interview-trap-performance)
6. [Handling Domain Object Permissions](#6-domain-object-permissions)
7. [ABAC in the Database Layer (ACL)](#7-acl)
8. [The Role of SpEL in ABAC](#8-spel-role)
9. [Integrating with External Policy Engines (OPA)](#9-opa)
10. [Testing Custom Permission Evaluators](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. RBAC vs ABAC
- **RBAC (Role-Based)**: "Admins can delete users." Access is based on *Who* you are.
- **ABAC (Attribute-Based)**: "Users can edit a document ONLY IF they are the owner AND the document status is 'DRAFT'." Access is based on *Attributes* of the user, the resource, and the environment.

---

## 2. What is PermissionEvaluator?
It is a Spring Security interface that allows you to plug in custom logic for the `hasPermission()` expression used in `@PreAuthorize`. It bridges the gap between high-level security expressions and your complex business logic.

---

## 3. Implementing Logic
You implement two methods:
1. `hasPermission(Authentication auth, Object targetDomainObject, Object permission)`
2. `hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission)`

---

## 4. hasPermission() Expression
In your controller:
`@PreAuthorize("hasPermission(#id, 'Document', 'READ')")`
This call is routed to your custom `PermissionEvaluator` bean.

---

## 5. The Classic Interview Trap: N+1 with hasPermission
**The Trap**: You use `hasPermission` in a `@PostFilter` or on every item in a list.
**The Problem**: For a list of 100 items, the `PermissionEvaluator` might call the database 100 times to check the "owner" attribute. This is the **N+1 Problem** applied to security.
**The Fix**: For lists, it is better to incorporate the security attributes directly into the SQL query (e.g., `WHERE owner_id = :userId`).

---

## 6. Domain Object Permissions
The evaluator can inspect the actual object instance. For example, it can check if `document.isPrivate()` is true and then only allow the owner to see it.

---

## 7. Spring Security ACL
For very complex requirements where every single row in the database needs its own specific permissions (e.g., Google Drive sharing), Spring provides an **ACL (Access Control List)** module. However, it is very heavy and often considered over-engineered for most apps.

---

## 8. SpEL and ABAC
SpEL allows you to pass anything to the evaluator:
`@PreAuthorize("hasPermission(#id, 'Document', 'WRITE') and #doc.status == 'DRAFT'")`

---

## 9. External Policy Engines
For enterprise-scale ABAC, many companies use **Open Policy Agent (OPA)**. You can implement a `PermissionEvaluator` that makes a REST call to OPA to get an allow/deny decision.

---

## 10. Testing
Since `PermissionEvaluator` is just a Spring Bean, you can unit test it by mocking the `Authentication` and `targetId` objects.

---

## 11. Common Mistakes
1. Making the evaluator too slow (it runs on every request).
2. Not handling the "ID-based" vs "Object-based" methods correctly.
3. Overusing ABAC where simple Roles would suffice.

---

## 12. Quick-Fire Interview Q&A
**Q: Where do I register the PermissionEvaluator?**  
A: In a `MethodSecurityExpressionHandler` bean.  
**Q: Can I have multiple PermissionEvaluators?**  
A: No, Spring Security only supports one. You must implement a "composite" or "delegating" evaluator if you need multiple logic sets.
