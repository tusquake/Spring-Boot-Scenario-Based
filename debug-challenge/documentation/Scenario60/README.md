# Scenario 60: Role Hierarchy

Simplifying security by allowing higher-level roles to inherit permissions from lower-level roles.

## Concept
In complex applications, an Admin should automatically be able to do everything a User or Guest can do. Instead of tagging every method with multiple roles (`@PreAuthorize("hasAnyRole('ADMIN', 'USER')")`), we define a relationship where `ADMIN > USER`.

## Implementation Details
We implemented a `RoleHierarchy` bean and registered it in the `MethodSecurityExpressionHandler`.

### Hierarchy Configuration:
```java
@Bean
public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
    return hierarchy;
}
```

## Verification Results
- **Endpoint**: `/api/scenario60/user-only` (Protected by `hasRole('USER')`).
- **Test 1**: Log in as `USER` -> **Allowed**.
- **Test 2**: Log in as `ADMIN` -> **Allowed** (Inherited access).
- **Test 3**: Log in as `Guest` -> **Denied** (403 Forbidden).

## Interview Theory: Why use Role Hierarchy?
- **Maintainability**: It keeps your code DRY (Don't Repeat Yourself). If you add a `SUPER_ADMIN` later, you just change the hierarchy string in one place instead of updating dozens of controllers.
- **Clarity**: It mirrors the real-world business structure where seniority implies broader access.
- **Syntax**: Use the `>` symbol to denote "includes". For example, `ROLE_ADMIN > ROLE_MANAGER > ROLE_USER`.
