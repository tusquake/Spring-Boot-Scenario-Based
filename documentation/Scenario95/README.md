# Scenario 95: JPA DTO Projections

## Overview
Why fetch every single column from the database if you only need a list of names and emails?

**DTO Projection** is a performance optimization that instructs Spring Data JPA to generate highly selective `SELECT` statements, reducing database CPU usage, network bandwidth, and application memory footprint.

---

## 🚀 The Concept: Interface Projections

Instead of returning a full Entity object (which can be huge), we define a simple Java Interface:

```java
public interface UserProjection {
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName(); // Open Projection (SpEL)
    
    String getEmail(); // Closed Projection
}
```

When you define a repository method that returns this interface:
```java
List<UserProjection> findAllProjectedBy();
```

Spring Data JPA recognizes the return type is an interface and automatically performs a selective SELECT.

---

## 🏗️ Closed vs. Open Projections

### 1. Closed Projections (Highly Optimized)
When the getter names in the interface **exactly match** the field names in the Entity.
*   **Result**: Hibernate optimizes the SQL to fetch only those columns.
*   **Example**: `getEmail()` matches `private String email`.

### 2. Open Projections (The SpEL Magic ✨)
When you use `@Value` to perform complex calculations on the data.
*   **Result**: Hibernate must fetch the fields needed for the calculation (e.g., `firstName`, `lastName`) and then Spring performs the logic in memory.
*   **Example**:
```java
@Value("#{target.firstName + ' ' + target.lastName}")
String getFullName();
```

---

## 📊 Comparison Table

| Feature | Full Entity (`SELECT *`) | DTO Projection (`SELECT firstName, lastName, email`) |
| :--- | :--- | :--- |
| **SQL Complexity** | High (Fetches all columns like BIO, ProfilePic, Address) | Low (Only requested columns) |
| **Memory Footprint** | Large (Hibernate must track the full object in Persistence Context) | Tiny (Read-only, lightweight object) |
| **Network Overhead** | High | Minimal |
| **Best For** | Editing/Updates | Read-only lists, Search results |

---

## 🧪 Testing the Scenario

### 1. Seed the Data
```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario95/seed"
```

### 2. Fetch Full Entities (Observe SQL)
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario95/users/full"
```
*In the logs, you will see Hibernate selecting every column.*

### 3. Fetch Projections (Optimized)
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario95/users/projection"
```
*In the logs, you will see a much cleaner SQL: `SELECT u1_0.first_name, u1_0.last_name, u1_0.email FROM scenario95_users u1_0`*

---

## Interview Tip 💡
**Q**: *"What are the different types of Projections in Spring Data JPA?"*  
**A**: *"There are **Interface-based** (what we used here) and **Class-based** (using simple POJOs/Records). Interface projections can even be 'Dynamic' by using generics in the repository method."*

**Q**: *"Can we use projections for complex calculations?"*  
**A**: *"Yes! You can use `@Value` with SpEL in the interface. For example: ` @Value("#{target.firstName + ' ' + target.lastName}") String getFullName();`"*
