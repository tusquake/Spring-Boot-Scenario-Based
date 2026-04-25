# Role Hierarchy in Spring Security — Complete Interview Reference

## Table of Contents
1. [What is Role Hierarchy?](#1-what-is-role-hierarchy)
2. [The Problem: Role Duplication](#2-the-problem)
3. [Configuring a RoleHierarchy Bean](#3-configuring-bean)
4. [Role Hierarchy Syntax (RoleA > RoleB)](#4-hierarchy-syntax)
5. [The Classic Interview Trap: Is Role Hierarchy Automatic?](#5-the-classic-interview-trap-automatic)
6. [Applying Role Hierarchy to Method Security](#6-applying-method-security)
7. [Combining Hierarchies (A > B and B > C)](#7-combining)
8. [Impact on hasRole vs hasAuthority](#8-impact-on-checks)
9. [Role Hierarchy and OAuth2/JWT](#9-oauth2-hierarchy)
10. [Performance of Recursive Role Checks](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Role Hierarchy?
Role Hierarchy is a feature that allows you to define logical relationships between roles. For example, you can state that `ROLE_ADMIN` includes all the permissions of `ROLE_USER`.

---

## 2. The Problem: Role Duplication
Without a hierarchy, if you have an endpoint that is accessible to both Admins and Users, you must list both:
`@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")`
This becomes unmanageable as the number of roles grows.

---

## 3. Configuring a RoleHierarchy Bean
You define the hierarchy by creating a bean of type `RoleHierarchy`:
```java
@Bean
public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER");
    return hierarchy;
}
```

---

## 4. Role Hierarchy Syntax
Use the `>` symbol to indicate that the role on the left "includes" the role on the right.
Multiple rules should be separated by newlines or a space.

---

## 5. The Classic Interview Trap: Is it Automatic?
**The Question**: *"Does Spring Security automatically assume ADMIN is superior to USER?"*
**The Answer**: **NO**. By default, all roles are flat and independent. You must explicitly configure a `RoleHierarchy` bean and register it with your `SecurityFilterChain`.

---

## 6. Method Security Integration
In Spring Security 6, once the `RoleHierarchy` bean is in the context, it is automatically picked up by the `MethodSecurityExpressionHandler`.

---

## 7. Combining Hierarchies
Hierarchies are transitive. If `ADMIN > STAFF` and `STAFF > USER`, then `ADMIN` automatically has `USER` permissions.

---

## 8. Impact on hasRole()
When you call `hasRole('USER')`, Spring Security checks if the user has `ROLE_USER` OR any role that is "higher" in the hierarchy (like `ROLE_ADMIN`).

---

## 9. Role Hierarchy and OAuth2
When using JWTs, your `GrantedAuthorities` are often extracted from the `scope` or `roles` claim. The Role Hierarchy applies to these extracted authorities just like it does to session-based users.

---

## 10. Performance
Spring Security caches the "expanded" authorities for a user once they are authenticated, so checking the hierarchy during every request has negligible performance impact.

---

## 11. Common Mistakes
1. Forgetting the `ROLE_` prefix in the hierarchy definition string.
2. Circular dependencies (e.g., `A > B` and `B > A`), which can cause infinite loops.
3. Not registering the `RoleHierarchy` bean with the `WebSecurityExpressionHandler`.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use Role Hierarchy with custom authorities (not starting with ROLE_)?**  
A: Yes, the hierarchy works with any string-based authority.  
**Q: How do I define multiple levels of hierarchy?**  
A: Use a newline-separated string: `"ROLE_SUPERADMIN > ROLE_ADMIN \n ROLE_ADMIN > ROLE_USER"`.
