# Multi-Tenancy (Data Isolation) — Complete Interview Reference

## Table of Contents
1. [What is Multi-Tenancy?](#1-what-is-multi-tenancy)
2. [Single-Tenant vs Multi-Tenant Architecture](#2-architecture)
3. [The Three Isolation Strategies](#3-isolation-strategies)
4. [Database-per-Tenant (Highest Isolation)](#4-db-per-tenant)
5. [Schema-per-Tenant](#5-schema-per-tenant)
6. [Discriminator Column (Shared Database)](#6-discriminator)
7. [The TenantContext and ThreadLocal](#7-tenant-context)
8. [The Classic Interview Trap: Data Leakage](#8-the-classic-interview-trap-leakage)
9. [Implementing a Multi-Tenant Datasource](#9-implementation)
10. [Multi-Tenancy in Hibernate (CurrentTenantIdentifierResolver)](#10-hibernate-resolver)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Multi-Tenancy?
Multi-tenancy is an architecture in which a single instance of a software application serves multiple customers (tenants). Each tenant's data is isolated and remains invisible to other tenants.

---

## 2. Architecture
- **Single-Tenant**: Each customer has their own dedicated server and database. (Expensive, but easiest to secure).
- **Multi-Tenant**: Customers share the same server resources but their data is logically separated. (Cost-effective and easier to maintain).

---

## 3. Isolation Strategies
1. **Database-per-tenant**: Each tenant has their own physical database.
2. **Schema-per-tenant**: All tenants share a database, but have their own schema (namespace).
3. **Column-per-tenant**: All tenants share the same tables, and a `tenant_id` column is used to filter data.

---

## 4. Database-per-Tenant
- **Pros**: Maximum security, easy to restore a single tenant's data, no "noisy neighbor" issues.
- **Cons**: High infrastructure cost, difficult to manage thousands of connections.

---

## 5. Schema-per-Tenant
A middle ground. It provides logical isolation while sharing the physical database resources. It's the default approach for many SaaS platforms using PostgreSQL.

---

## 6. Discriminator Column
- **Pros**: Lowest cost, easiest to scale to millions of tenants.
- **Cons**: Highest risk of data leakage if a developer forgets to add `WHERE tenant_id = ?` to a query.

---

## 7. TenantContext
In a Spring Boot app, the `tenantId` is usually extracted from a header (e.g., `X-Tenant-ID`) or a JWT claim and stored in a `ThreadLocal` variable using a `Filter` or `Interceptor`. This allows the database layer to know which tenant it is working for.

---

## 8. The Classic Interview Trap: Query Pollution
**The Question**: *"How do you ensure a developer doesn't accidentally fetch all tenants' data in a shared-database model?"*
**The Answer**: Do NOT rely on manual filtering. Use Hibernate's `@Filter` or `@TenantId` (Hibernate 6) feature, or use a **Spring Data Aspect** that automatically appends the `tenant_id` to every query at the framework level.

---

## 9. Multi-Tenant Datasource
You can implement `AbstractRoutingDataSource` in Spring. This allows the application to switch the database connection dynamically at runtime based on the `tenantId` found in the `TenantContext`.

---

## 10. Hibernate Resolver
Hibernate provides `CurrentTenantIdentifierResolver` and `MultiTenantConnectionProvider` interfaces. When these are implemented, Hibernate handles the switching of schemas or databases automatically.

---

## 11. Common Mistakes
1. Not clearing the `ThreadLocal` TenantContext after a request (can lead to data leakage in a thread pool).
2. Hardcoding tenant IDs in queries.
3. Not testing cross-tenant data access during security audits.

---

## 12. Quick-Fire Interview Q&A
**Q: Which strategy is best for a small SaaS startup?**  
A: Discriminator Column (Shared Database) because it has the lowest operational overhead.  
**Q: What is a "Noisy Neighbor"?**  
A: When one tenant performs heavy queries that slow down the database for all other tenants.
