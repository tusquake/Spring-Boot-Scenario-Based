# SQL Injection & Database Security — Complete Interview Reference

## Table of Contents
1. [What is SQL Injection?](#1-what-is-sql-injection)
2. [How SQL Injection Works (String Concatenation)](#2-how-it-works)
3. [Parameterized Queries (Prepared Statements)](#3-prepared-statements)
4. [JPA & Hibernate: Are they safe by default?](#4-jpa-hibernate-safety)
5. [The Classic Interview Trap: Vulnerable @Query](#5-the-classic-interview-trap-query)
6. [SQL Injection in Stored Procedures](#6-stored-procedures)
7. [Second-Order SQL Injection](#7-second-order)
8. [Blind SQL Injection (Boolean and Time-based)](#8-blind-sql)
9. [Database-level Defenses (Privileges)](#9-database-defenses)
10. [Tools for Detecting SQL Injection (sqlmap)](#10-detection-tools)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is SQL Injection?
SQL Injection (SQLi) is a type of vulnerability where an attacker can interfere with the queries that an application makes to its database. It can allow attackers to view data they are not normally able to retrieve, or even modify or delete it.

---

## 2. How SQL Injection Works
Injection happens when user input is directly concatenated into a SQL string:
- **Code**: `String sql = "SELECT * FROM users WHERE name = '" + name + "'";`
- **Attack**: `name = "' OR '1'='1"`
- **Result**: `SELECT * FROM users WHERE name = '' OR '1'='1'` (Returns all users).

---

## 3. Prepared Statements
The primary defense against SQLi. A prepared statement sends the SQL query structure and the data parameters to the database separately. The database engine never interprets the data as code.
```java
jdbcTemplate.query("SELECT * FROM users WHERE name = ?", name);
```

---

## 4. JPA & Hibernate Safety
JPA is generally safe because it uses `PreparedStatement` under the hood. However, you can still write vulnerable code if you use string concatenation inside `entityManager.createQuery()` or `nativeQuery`.

---

## 5. The Classic Interview Trap: Vulnerable @Query
**The Trap**: You use a custom `@Query` in a Spring Data repository.
**The Problem**: If you use `nativeQuery = true` and manually concatenate strings:
`@Query(value = "SELECT * FROM users WHERE name = " + name, nativeQuery = true)`
**The Fix**: Always use positional (`?1`) or named (`:name`) parameters.
`@Query("SELECT u FROM User u WHERE u.name = :name")`

---

## 6. Stored Procedures
Stored procedures are not automatically safe. If a stored procedure uses dynamic SQL (`EXECUTE IMMEDIATE`) and concatenates its inputs into that SQL string, it is still vulnerable to injection.

---

## 7. Second-Order SQL Injection
This happens when an application stores malicious input in the database and later uses that input in a different, vulnerable query. The attack doesn't happen when the data is saved, but when it is reused.

---

## 8. Blind SQL Injection
- **Boolean-based**: Attacker asks true/false questions to the DB (e.g., "Does the admin password start with A?") and observes the response.
- **Time-based**: Attacker tells the DB to pause if a condition is true (e.g., `IF(1=1, SLEEP(10), 0)`). If the page takes 10 seconds to load, they know the condition was true.

---

## 9. Database-level Defenses
The principle of **Least Privilege**. The database user used by the application should only have permissions to the tables it needs and should NOT be a `db_owner` or `superuser`.

---

## 10. Detection Tools
- **sqlmap**: An open-source tool that automates the process of detecting and exploiting SQL injection flaws.
- **Static Analysis (SAST)**: Tools like SonarQube can find string concatenation in SQL calls during code review.

---

## 11. Common Mistakes
1. Trusting internal data (data coming from the DB can also be malicious if not sanitized).
2. Using `Statement` instead of `PreparedStatement`.
3. Concatenating column names or table names (Prepared statements only work for values, not for metadata).

---

## 12. Quick-Fire Interview Q&A
**Q: Can you perform SQL injection on a NoSQL database like MongoDB?**  
A: Yes. It is called "NoSQL Injection" and often involves injecting JSON operators like `$gt: ""`.  
**Q: Does ORM eliminate SQL injection?**  
A: No, it reduces it, but improper use of JPQL or Native Queries can still lead to vulnerabilities.
