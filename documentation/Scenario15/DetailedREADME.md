# Flyway Database Migrations — Complete Interview Reference

## Table of Contents
1. [What is Database Migration?](#1-what-is-database-migration)
2. [Why Flyway over Manual SQL?](#2-why-flyway)
3. [How Flyway Works (The schema_history Table)](#3-how-flyway-works)
4. [Migration Naming Conventions](#4-naming-conventions)
5. [The Classic Interview Trap: Modifying a Migration](#5-the-classic-interview-trap-modifying)
6. [Handling Migration Failures](#6-handling-failures)
7. [Flyway vs Liquibase](#7-flyway-vs-liquibase)
8. [Repeatable Migrations (R__ scripts)](#8-repeatable-migrations)
9. [Baseline and Repair Commands](#9-baseline-repair)
10. [Environment-Specific Migrations](#10-env-specific)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Database Migration?
Database migration is version control for your database schema. It allows you to keep the database in sync with your Java code as it evolves over time.

---

## 2. Why Flyway over Manual SQL?
- **Automation**: Schema changes are applied automatically on application startup.
- **Consistency**: Ensures every developer and environment (Dev, QA, Prod) has the exact same schema.
- **Rollback Safety**: Prevents accidental re-execution of scripts.

---

## 3. How Flyway Works (The schema_history Table)
Flyway creates a table named `flyway_schema_history`. It records the version, description, and **checksum** of every script it executes. Before running a new script, it checks this table to see what has already been applied.

---

## 4. Migration Naming Conventions
Scripts must follow a specific pattern: `V<Version>__<Description>.sql` (e.g., `V1__init_schema.sql`). Note the **double underscore**.

---

## 5. The Classic Interview Trap: Modifying a Migration
**The Trap**: You applied `V2__add_email.sql` to production. Now you realize you made a typo. You change the file and restart the app.
**The Result**: Flyway will **FAIL** because the checksum of the file on disk no longer matches the checksum recorded in the `schema_history` table.
**The Fix**: NEVER modify an existing migration. Always create a new one (e.g., `V3__fix_email_typo.sql`).

---

## 6. Handling Migration Failures
If a migration fails halfway, the database might be left in a "dirty" state. You must manually fix the data and use the `flyway repair` command to clear the failed entry from the history table.

---

## 7. Flyway vs Liquibase
- **Flyway**: Simpler, uses plain SQL. Great for developers who love SQL.
- **Liquibase**: More complex, uses XML/YAML/JSON. Offers better database-agnosticism (auto-generates SQL for Postgres/MySQL/Oracle).

---

## 8. Repeatable Migrations (R__ scripts)
Repeatable migrations (e.g., `R__refresh_views.sql`) have no version number. They are re-executed by Flyway every time their checksum changes. Perfect for Views, Stored Procedures, and Functions.

---

## 9. Baseline and Repair Commands
- **Baseline**: Tells Flyway to start tracking an *existing* database from a specific version.
- **Repair**: Fixes the schema history table after a checksum mismatch or a failed migration.

---

## 10. Environment-Specific Migrations
You can use Spring Profiles to control which migrations run in which environment by placing them in different folders or using placeholders in the SQL.

---

## 11. Common Mistakes
1. Using a single underscore instead of double.
2. Modifying a script after it has been pushed to a shared environment.
3. Not including a `flyway repair` step in the CI/CD pipeline when needed.

---

## 12. Quick-Fire Interview Q&A
**Q: Can Flyway roll back a migration?**  
A: The community version does not support automatic rollbacks. You must write a "undo" migration manually or use the paid "Teams" edition.  
**Q: What happens if two developers create V4?**  
A: Flyway will throw an error. Teams should coordinate on version numbers or use timestamps (e.g., `V202310271030__init.sql`) to avoid collisions.
