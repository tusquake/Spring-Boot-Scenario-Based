# Scenario 15: Project Data Listing

A simple baseline scenario demonstrating standard JPA repository usage.

## Concept
Standard "Read All" operation showing how Spring Data JPA provides boilerplate repository methods for free.

## Implementation Details
- **Model**: `ProjectData` representing a simple project record.
- **Controller**: Returns a list of all projects directly from the repository.

## Verification Results
- **URL**: `http://localhost:8080/api/scenario15/list`
- **Result**: A JSON array of project objects.

## Interview Theory: Spring Data JPA
- **JpaRepository vs CrudRepository**: `JpaRepository` extends `PagingAndSortingRepository`, giving you built-in support for pagination and flushing the persistence context.
- **Query Methods**: Mention that you can define custom queries just by the method name, like `findByProjectNameContaining()`.
- **Performance**: For large datasets, always use `Pageable` instead of `findAll()` to avoid loading millions of records into RAM.
