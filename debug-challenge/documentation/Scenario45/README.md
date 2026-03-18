# Scenario 45: Hibernate Envers (Full History Audit)

Demonstrates how to maintain a complete version history (Revisions) of your data, allowing you to "Go back in time."

## Concept
JPA Auditing (Scenario 42) only shows the *latest* update. **Hibernate Envers** creates dedicated `_AUD` tables that store a new row for every single change, including what the data looked like before.

## Implementation Details
1. Added `@Audited` to the `Customer` entity.
2. Used `AuditReader` to query the revision history.

### Query Snippet:
```java
AuditReader reader = AuditReaderFactory.get(entityManager);
List<Object[]> history = reader.createQuery()
    .forRevisionsOfEntity(Customer.class, false, true)
    .getResultList();
```

## Verification Results
- **Action**: Update a customer's name using `/api/scenario45/update/1/NewName`.
- **History**: Check `/api/scenario45/history/1`.
- **Result**: You see an array of objects showing "Rev 1: OriginalName" and "Rev 2: NewName".

## Interview Theory: Data Forensics
- **Storage Cost**: Warn that Envers grows your database quickly because every update duplicates the row. Use it only for critical tables like "Payments" or "User Permissions."
- **Point-in-time**: Envers allows you to fetch an entity exactly as it appeared at a specific timestamp or revision ID.
- **Audit Tables**: Mention that Hibernate automatically creates tables like `customers_aud` and `revinfo` (global revision tracker).
