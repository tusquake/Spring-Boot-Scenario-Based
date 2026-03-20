# Scenario 80: Custom Queries in Spring Data JPA

## Overview
Spring Data JPA provides several high-level ways to fetch data without writing complex boilerplate code. Understanding when to use which method is key for both performance and maintainability.

## Query Strategies 🔍

### 1. Derived Query Methods
Spring parses the method name and converts it into a SQL query automatically.
```java
List<Scenario80Book> findByAuthorIgnoreCase(String author);
```
- **Pros**: Zero boiler-plate, very readable for simple filters.
- **Cons**: Method names can become very long for complex conditions.

### 2. JPQL (@Query)
Jakarta Persistence Query Language (JPQL) is an object-oriented query language. You write queries against your **Entities**, not your database tables.
```java
@Query("SELECT b FROM Scenario80Book b WHERE b.price > :minPrice")
List<Scenario80Book> findExpensiveBooks(@Param("minPrice") Double minPrice);
```
- **Pros**: Database agnostic (works on H2, MySQL, Oracle similarly), supports complex joins.
- **Cons**: Still an abstraction; might not support very specific DB features like recursive CTEs.

### 3. Native SQL (@Query)
Allows you to write raw SQL that runs directly against your database tables.
```java
@Query(value = "SELECT * FROM scenario80book WHERE author LIKE %:keyword%", nativeQuery = true)
List<Scenario80Book> findBooksByAuthorNative(@Param("keyword") String keyword);
```
- **Pros**: Performant, supports DB-specific features (e.g., JSONB in Postgres).
- **Cons**: Tied to a specific database (cannot easily switch from H2 to Oracle).

### 4. Modifying Queries (@Modifying)
Used for `UPDATE` or `DELETE` operations. **Crucial**: Must be used together with `@Transactional`.
```java
@Modifying
@Transactional
@Query("UPDATE Scenario80Book b SET b.price = b.price * :factor")
int updateAllPrices(@Param("factor") Double factor);
```

## Interview Tips 💡
- **@Param**: Use this to map method arguments to named parameters in your query (e.g., `:minPrice`).
- **Positional Parameters**: You can use `?1`, `?2`, but named parameters are generally preferred for readability.
- **DTO Projections**: You can use JPQL to fetch only specific fields into a custom DTO instead of the whole entity (useful for performance).

## Testing the Scenario
1. **Search by Author**: `GET /api/scenario80/search/author?name=Craig Walls`
2. **Search Expensive Books**: `GET /api/scenario80/search/expensive?min=52.0`
3. **Keyword Native Search**: `GET /api/scenario80/search/native?keyword=Craig`
4. **Update Prices**: `POST /api/scenario80/update-prices?factor=1.1`
