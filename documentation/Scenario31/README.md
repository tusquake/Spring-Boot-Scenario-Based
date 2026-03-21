# Scenario 31: Dynamic Search (JPA Specifications)

Demonstrates the "Tinder Filter" approach: Building a query dynamically based on which search filters the user fills out.

## Concept
Writing `findByLevelAndNameAndEmailAnd...` with 20 optional parameters is a nightmare. **JPA Specifications** allow you to build predicates (the `WHERE` clause) programmatically using the Criteria API.

## Implementation Details
We take optional params (name, email, minId, maxId) and combine them using a `Specification`.

### Code Snippet:
```java
Specification<Customer> spec = (root, query, cb) -> {
    List<Predicate> predicates = new ArrayList<>();
    if (name != null) predicates.add(cb.like(root.get("name"), "%" + name + "%"));
    if (minId != null) predicates.add(cb.ge(root.get("id"), minId));
    return cb.and(predicates.toArray(new Predicate[0]));
};
return customerRepository.findAll(spec);
```

## Verification Results
- **Search by Name**: `/api/scenario31/search?name=Alice`
- **Range Search**: `/api/scenario31/search?minId=10&maxId=50`
- **Combined**: `/api/scenario31/search?name=A&minId=5`
- **Result**: Returns only records matching all active filters.

## Interview Theory: Specifications
- **The Advantages**: Very reusable. You can define a `CustomerSpecs.hasActivePlan()` and combine it with a search specification using `.and()`.
- **Criteria API vs JPQL**: While JPQL is easier to read for static queries, the Criteria API is much safer and more powerful for dynamic, multi-filter searches.
- **Performance**: Ensure the columns you search by most often are indexed in the database!
