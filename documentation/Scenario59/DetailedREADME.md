# hasRole vs hasAuthority — Complete Interview Reference

## Table of Contents
1. [The Authority Hierarchy in Spring Security](#1-authority-hierarchy)
2. [What is hasAuthority()?](#2-has-authority)
3. [What is hasRole()?](#3-has-role)
4. [The ROLE_ Prefix Mystery](#4-role-prefix)
5. [The Classic Interview Trap: hasRole('ROLE_ADMIN')](#5-the-classic-interview-trap-hasrole)
6. [Mapping Authorities from JWTs/OAuth2](#6-mapping-authorities)
7. [DefaultRolePrefixFilter (Spring 4+)](#7-default-role-prefix)
8. [Customizing the Role Prefix](#8-custom-prefix)
9. [Which One Should You Use?](#9-which-one)
10. [Combining Multiple Roles/Authorities](#10-combining)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. The Authority Hierarchy
In Spring Security, everything is ultimately a `GrantedAuthority`. Whether it's a role (`ROLE_ADMIN`), a scope (`SCOPE_read`), or a permission (`FILE_DELETE`), they are all stored as simple strings in a list attached to the `Authentication` object.

---

## 2. What is hasAuthority()?
`hasAuthority(string)` checks for an **EXACT** string match against the user's granted authorities.
- `hasAuthority('ROLE_ADMIN')` matches `ROLE_ADMIN`.
- `hasAuthority('SCOPE_read')` matches `SCOPE_read`.

---

## 3. What is hasRole()?
`hasRole(string)` is a convenience method. It automatically appends the `ROLE_` prefix to your string before checking.
- `hasRole('ADMIN')` checks for `ROLE_ADMIN`.

---

## 4. The ROLE_ Prefix Mystery
Spring Security has a long-standing convention of prefixing roles with `ROLE_`. This distinguishes "who you are" (Roles) from "what you can do" (Permissions/Authorities).

---

## 5. The Classic Interview Trap: hasRole('ROLE_ADMIN')
**The Trap**: You use `@PreAuthorize("hasRole('ROLE_ADMIN')")`.
**The Problem**: Spring appends `ROLE_` again, resulting in a check for `ROLE_ROLE_ADMIN`. This will almost always fail.
**The Fix**: Use `hasRole('ADMIN')` OR `hasAuthority('ROLE_ADMIN')`.

---

## 6. Mapping Authorities from JWTs
When using OAuth2/JWT, the scopes in the token are usually mapped as `SCOPE_read`, `SCOPE_write`. You should use `hasAuthority('SCOPE_read')` for these. If you want to use them as roles, you must configure a `JwtAuthenticationConverter`.

---

## 7. DefaultRolePrefixFilter
In modern Spring Security, the `ROLE_` prefix is handled by the `DefaultRolePrefixFilter` or similar logic in the `AuthorityAuthorizationManager`.

---

## 8. Customizing the Role Prefix
You can change or remove the default `ROLE_` prefix by configuring the `GrantedAuthorityDefaults` bean:
```java
@Bean
public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
}
```

---

## 9. Which One Should You Use?
- Use **hasRole** for high-level user categories (Admin, User, Manager).
- Use **hasAuthority** for fine-grained permissions (OP_DELETE_USER, SCOPE_READ).

---

## 10. Combining Rules
You can combine them using boolean logic:
`@PreAuthorize("hasRole('ADMIN') or hasAuthority('SCOPE_write')")`

---

## 11. Common Mistakes
1. Adding the `ROLE_` prefix manually inside `hasRole()`.
2. Assuming `hasRole('ADMIN')` will match a database value of `ADMIN` (it won't, it needs `ROLE_ADMIN`).
3. Forgetting that authorities are case-sensitive.

---

## 12. Quick-Fire Interview Q&A
**Q: Can a user have both Roles and Authorities?**  
A: Yes, they are all stored together in the same collection.  
**Q: What is the benefit of using the ROLE_ prefix?**  
A: It provides a standard convention that separates identity (Role) from capability (Authority).
