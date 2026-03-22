# Scenario 91: Multi-Tenant Architecture (AbstractRoutingDataSource)

## Overview
**Multi-Tenancy** is an architecture where a single instance of a software application serves multiple customers (tenants). Each tenant's data is isolated from others, even though they share the same application logic.

This scenario demonstrates **Database-per-Tenant** isolation using Spring's `AbstractRoutingDataSource`.

---

## 🏗️ Core Components

### 1. `TenantContext`
Uses `ThreadLocal<String>` to store the current tenant ID. This ensures that the tenant information is thread-safe and isolated between different concurrent requests.

### 2. `MultiTenantRoutingDataSource`
Extends `AbstractRoutingDataSource`. It overrides the `determineCurrentLookupKey()` method to dynamically return the `tenantId` from the `TenantContext` at the exact moment a database connection is needed.

### 3. `TenantInterceptor`
A `HandlerInterceptor` that intercepts every incoming HTTP request. It looks for the **`X-Tenant-ID`** header and populates the `TenantContext`. It also ensures the context is cleared after the request finishes to prevent memory leaks.

---

## 🛠️ Implementation Details

### Data Source Routing logic:
```java
public class MultiTenantRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getTenantId(); // Returns "tenantA", "tenantB", etc.
    }
}
```

### Configuration:
- **Default Database**: `jdbc:h2:mem:defaultdb`
- **Tenant A Database**: `jdbc:h2:mem:tenantadb`
- **Tenant B Database**: `jdbc:h2:mem:tenantbdb`

---

## 🧪 Testing the Isolation

Use the following `curl` commands to see how data is isolated across different "tenants" despite hitting the same endpoint.

### Step 1: Create data for Tenant A
```bash
curl -X POST http://localhost:8080/api/scenario91/projects \
     -H "X-Tenant-ID: tenantA" \
     -H "Content-Type: application/json" \
     -d '{"name": "Project Alpha", "description": "Tenant A Secret"}'
```

### Step 2: Create data for Tenant B
```bash
curl -X POST http://localhost:8080/api/scenario91/projects \
     -H "X-Tenant-ID: tenantB" \
     -H "Content-Type: application/json" \
     -d '{"name": "Project Beta", "description": "Tenant B Secret"}'
```

### Step 3: Verify Tenant A's View
```bash
curl -H "X-Tenant-ID: tenantA" http://localhost:8080/api/scenario91/projects
```
*Expected: Only shows Project Alpha.*

### Step 4: Verify Tenant B's View
```bash
curl -H "X-Tenant-ID: tenantB" http://localhost:8080/api/scenario91/projects
```
*Expected: Only shows Project Beta.*

---

## Interview Tip 💡
**Q**: *"What are the different ways to achieve multi-tenancy in Spring Boot?"*  
**A**: *"There are three main patterns:*
1.  * **Database per Tenant**: Highest isolation, easiest for compliance, but most expensive/complex to manage.
2.  * **Schema per Tenant**: Good balance; one DB instance, many schemas.
3.  * **Shared Database, Shared Schema**: Use a `tenant_id` column on every table. Highest density, but riskier if filters are forgotten."*

**Q**: *"What is the benefit of `AbstractRoutingDataSource`?"*  
**A**: *"It abstracts the switching logic from the business code and Hibernate. The application doesn't need to know which DB it's talking to; the router handles it just before the query execution."*
