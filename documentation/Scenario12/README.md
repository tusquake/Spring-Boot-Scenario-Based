# Scenario 12: Soft Delete with Hibernate

Demonstrates how to "delete" records from a user's perspective while keeping the data safely in the database.

## Concept
In many enterprise apps, data is never truly deleted for audit reasons. Instead, a `deleted` flag is set to `true`. Hibernate provides annotations to automate this process so you don't have to manually filter queries.

## Implementation Details
We used `@SQLDelete` to override the standard SQL `DELETE` command and `@SQLRestriction` to filter out deleted items automatically.

### Entity Snippet:
```java
@Entity
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class SoftDeleteProduct {
    private boolean deleted = false;
}
```

## Verification Results
1. **Delete**: Call `/api/scenario12/delete/1`.
   - **Action**: Hibernate runs an `UPDATE`, not a `DELETE`.
2. **Fetch**: Call `/api/scenario12/all`.
   - **Action**: Product 1 is missing, even though it's still in the DB.
3. **Audit**: Call `/api/scenario12/all-including-deleted` (Native Query).
   - **Action**: Product 1 reappears!

## Interview Theory: Soft Delete
- **The Caveats**: Unique constraints (like `email`) can be tricky with soft delete because a "deleted" alice@example.com still prevents a new Alice from signing up.
- **Handling Associations**: If you soft-delete a parent, you usually need to decide if you'll also soft-delete children (Cascade).
- **Native Queries**: Soft delete filters are often ignored by native SQL queries, so keep that in mind during report generation.
