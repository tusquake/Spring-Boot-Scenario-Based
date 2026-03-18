# Scenario 65: SQL Injection (SQLi) Prevention

Demonstrates how to protect your database from one of the most common and dangerous web vulnerabilities.

## Concept
**SQL Injection** happens when user input is directly concatenated into a SQL string. An attacker can use special characters (like `'`, `OR`, `--`) to change the logic of the query.

Example: `SELECT * FROM users WHERE name = '' OR '1'='1'`
This query always returns true, allowing an attacker to bypass authentication or dump the entire database.

## Implementation Details
We demonstrated three approaches:
1.  **Vulnerable**: Uses raw string concatenation with `JdbcTemplate`.
2.  **Secure (JDBC)**: Uses **Parameterized Queries** (Prepared Statements) where the input is treated strictly as data.
3.  **Secure (JPA)**: Uses Spring Data JPA, which uses prepared statements under the hood by default.

### Vulnerable Snippet:
```java
String sql = "SELECT * FROM customers WHERE name = '" + name + "'";
return jdbcTemplate.queryForList(sql);
```

### Secure Snippet (Prepared Statement):
```java
String sql = "SELECT * FROM customers WHERE name = ?";
return jdbcTemplate.queryForList(sql, name);
```

## Verification Results
- **Test Payload**: `' OR '1'='1`
- **Vulnerable Result**: Returns **ALL 100 CUSTOMERS** from the database (Injection Successful).
- **Secure Result**: Returns **0 Results** (Injection Blocked). The database searched for a customer whose literal name was exactly the string `' OR '1'='1`.

## Interview Theory: SQLi Prevention
- **Primary Defense**: Always use **Parameterized Queries** (Prepared Statements).
- **Secondary Defense**: Use an ORM like **Spring Data JPA** or **Hibernate**, which handles parameterization for you.
- **Least Privilege**: Ensure the database user your app uses only has the permissions it needs (e.g., no `DROP TABLE` permissions).

---

## 🏛️ Special Topic: Stored Procedures

A **Stored Procedure** is a "function" that lives inside your database. Instead of writing SQL in Java, you call the procedure by name.

### Are they safe?
**Yes, if used correctly.** 
Most procedures use parameters, which enforce the "Data vs Command" separation just like Prepared Statements. However, if a procedure uses **Dynamic SQL** (concatenation) internally, it is **still vulnerable**.

### Pros & Cons
- **Pros**: 
  - **Performance**: Pre-compiled by the DB.
  - **Security**: You can grant permission to execute the *procedure* without giving access to the *tables*.
  - **Traffic**: Only the call command is sent over the network.
- **Cons**: 
  - **Portability**: Different syntax for Oracle, MySQL, etc.
  - **Maintenance**: Business logic is split between Java and SQL files.
