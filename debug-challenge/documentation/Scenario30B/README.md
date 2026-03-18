# Scenario 30B: Dynamic Sorting & Pagination

A practical implementation of the pagination concepts using a searchable Gadget inventory.

## Concept
Dynamic sorting allows users to click a column header (Price, Name, Category) and have the API sort the results without writing multiple repository methods.

## Implementation Details
Uses `PageRequest.of(page, size, sort)` to combine all three user-provided parameters into a single JPA query.

### Controller Snippet:
```java
Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
return gadgetRepository.findAll(PageRequest.of(page, size, sort));
```

## Verification Results
- **URL**: `/api/scenario30b/gadgets?sortBy=price&direction=desc&size=5`
- **Result**: Returns the top 5 most expensive gadgets first.
- **Observation**: The `Gadget` objects are returned in a `Page` wrapper, providing metadata like `totalPages` and `totalElements`.

## Interview Theory: Sorting Best Practices
- **White-listing**: Never pass the `sortBy` parameter directly to a custom SQL query (SQL Injection risk). JPA `Sort` is safe because it maps to entity properties, but you should still validate that the property exists.
- **Multiple Sorts**: Mention that JPA supports `Sort.by(sortBy).and(Sort.by(anotherField))` for secondary sorting (e.g., sort by Category, then by Price).
- **Hardcoding Defaults**: Always provide sensible defaults (e.g., `id` ascending) so the results are consistent even if the user provides no parameters.
