# Scenario 65: SQL Injection Prevention

SQL Injection is a vulnerability where an attacker can interfere with the queries that an application makes to its database. 

## Vulnerable vs Secure Implementation

### 1. The Vulnerable Way (String Concatenation)
Never build SQL queries by concatenating strings with user input!
```java
String sql = "SELECT * FROM customers WHERE name = '" + name + "'";
```
**Attack**: Inputting `' OR '1'='1` will return all records.

### 2. The Secure Way (Parameterized Queries)
Always use `?` placeholders. The database treates the input strictly as data, not executable code.
```java
String sql = "SELECT * FROM customers WHERE name = ?";
return jdbcTemplate.queryForList(sql, name);
```

### 3. Using Spring Data JPA
JPA Repositories are secure by default because they use parameter binding under the hood.

### 4. Stored Procedures (Calling via CALL)
Even with stored procedures, you must use parameterized calls to ensure safety.

---

## Interview Theory: SQLi Prevention
- **Primary Defense**: Always use **Parameterized Queries** (Prepared Statements).
- **Secondary Defense**: Use an ORM like **Spring Data JPA** or **Hibernate**.
- **Least Privilege**: Ensure the database user used by the app only has necessary permissions (e.g., cannot drop tables).

## How to Test
- **Vulnerable**: `curl "http://localhost:8080/api/scenario65/vulnerable/search?name=' OR '1'='1"`
- **Secure**: `curl "http://localhost:8080/api/scenario65/secure/jdbc/search?name=' OR '1'='1"` (Returns empty or valid match only)
