# Scenario 30: Advanced Pagination (Page vs Slice vs Cursor)

Demonstrates the three main ways to handle large datasets in Spring Data JPA, moving from simple offset pagination to high-performance cursors.

## Concept
1. **Page**: The most common. Issues two queries: one for data and one for the total count (`SELECT COUNT(*)`). Allows "Jumping" to a page (e.g., Page 5).
2. **Slice**: Optimized for "Infinite Scroll." It doesn't fetch the total count. It just asks for `size + 1` records to see if there's more data.
3. **Cursor (Keyset)**: The most performant. Instead of `OFFSET`, it uses `WHERE id > last_seen_id`. Constant time performance even on millions of records.

## Implementation Details
We used `CustomerRepository` to demonstrate all three types.

### Cursor Snippet (Spring Data 3):
```java
Window<Customer> window = customerRepository.findFirst10ByOrderByIdAsc(position);
```

## Verification Results
- **Page**: `/api/scenario30/page` -> Returns content + `totalElements`.
- **Slice**: `/api/scenario30/slice` -> Returns content + `hasNext`. Faster as it skips the count query.
- **Cursor**: `/api/scenario30/cursor` -> Returns a `nextCursor` string. Use this string for the next request to get the next batch with zero performance degradation.

## Interview Theory: Scaling Pagination
- **The Problem with OFFSET**: `OFFSET 1000000 LIMIT 10` is slow because the database must still scan 1,000,000 rows only to discard them.
- **When to use Slice?**: Use it for mobile feeds or any UI where the user doesn't need to know the exact total number of pages.
- **Spring Data 3 Window**: Mention the new `Window` and `ScrollPosition` APIs introduced in Spring Data 3 for elegant keyset pagination.
