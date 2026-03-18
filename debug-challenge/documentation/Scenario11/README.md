# Scenario 11: Validation Groups

Demonstrates how to apply different validation rules to the same model based on the operation (e.g., Create vs Update).

## Concept
Sometimes, a field is mandatory during creation (like a password) but optional during an update. Instead of creating two different DTOs, you can use **Validation Groups**.

## Implementation Details
We used two custom marker interfaces, `OnCreate` and `OnUpdate`, to group constraints.

### Model Snippet:
```java
@NotBlank(groups = OnCreate.class)
private String password;

@NotBlank(groups = {OnCreate.class, OnUpdate.class})
private String email;
```

### Controller Snippet:
```java
@PostMapping("/create")
public String createUser(@Validated(OnCreate.class) @RequestBody ValidationUser user) { ... }

@PutMapping("/update")
public String updateUser(@Validated(OnUpdate.class) @RequestBody ValidationUser user) { ... }
```

## Verification Results
1. **Create User**: Call `/api/scenario11/create` without a password.
   - **Result**: `400 Bad Request` (Validation Failed).
2. **Update User**: Call `/api/scenario11/update` without a password.
   - **Result**: `Success` (Validation passed because password isn't in the `OnUpdate` group).

## Interview Theory: Bean Validation
- **Custom Constraints**: You can create your own annotations (like `@ValidEmail`) by implementing `ConstraintValidator`.
- **Validation Messages**: Use `ValidationMessages.properties` to externalize error messages.
- **Group Inheritance**: Groups can inherit from `Default.class` if you want some fields to always be validated regardless of the group.
