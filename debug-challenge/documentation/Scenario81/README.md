# Scenario 81: Auditing Support with Spring Data JPA

## Overview
**Auditing** is the process of tracking who created or modified an entity and when it happened. Spring Data JPA provides built-in support for this, which significantly reduces boilerplate code and ensures consistency across all database tables.

## Key Components 🕵️‍♂️

### 1. @EnableJpaAuditing
This annotation is required in a configuration class to activate auditing. It can also point to an `AuditorAware` bean if you want to track the current user.

### 2. Base Auditable Entity (@MappedSuperclass)
Instead of adding audit fields to every entity, we create a base class.
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditableEntity {
    @CreatedDate
    private LocalDateTime createdDate;
    
    @CreatedBy
    private String createdBy;
    
    // ... same for modified
}
```
- **@EntityListeners**: This is mandatory. It hooks into the JPA lifecycle to set the values before saving.

### 3. AuditorAware Implementation
This interface defines who the "current auditor" is.
- In production, it extracts the username from `SecurityContextHolder`.
- In our demo, it returns `Tushar (Mock)`.

## Interview Tips 💡
- **Why use Auditing?** For security, debugging, and data integrity. Knowing *exactly* when a record changed and who changed it is essential for production support.
- **Can I audit something other than strings/dates?** Yes, you can audit any type, but String and LocalDateTime/Instant are the most common.
- **Difference from Envers?** JPA Auditing only stores the *current* metadata (who did the last change). **Hibernate Envers** stores a full version history (a separate entry for every change).

## Testing the Scenario
1. **Create a Product**:
   `POST /api/scenario81/products`
   Body: `{"name": "AuditPhone", "price": 999.0}`
   - **Check the response**: Notice `createdBy: Tushar (Mock)` and `createdDate` are populated automatically.

2. **Update a Product**:
   `PUT /api/scenario81/products/1`
   Body: `{"name": "AuditPhone Pro", "price": 1099.0}`
   - **Check the response**: Notice `lastModifiedDate` is now populated and different from `createdDate`.
