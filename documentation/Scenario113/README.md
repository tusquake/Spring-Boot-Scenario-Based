# Scenario 113: Spring Data REST Masterclass 🌐

## Overview
How do you build a REST API with zero boilerplate?

**Spring Data REST** is a powerful project that automatically exports your JPA repositories as RESTful endpoints. It follows **HATEOAS** (Hypermedia as the Engine of Application State) principles, providing a self-discoverable API.

---

## 🚀 Key Features

### 1. Automatic Discovery 🗺️
The root of the API (`/api/data-rest`) provides links to all available collections.
- **Benefit**: Clients don't need to hardcode URLs. They can "follow the links."

### 2. Projections (The "Hider") 🪄
One of the most powerful features. It allows you to create different views of the same data.
- **Example**: In our `Scenario113Employee` entity, we have a **salary** field. 
- **The Problem**: You don't want to show the salary to everyone.
- **The Solution**: Use a **Projection** (`Scenario113EmployeeProjection`) to only show `name` and `position`.

### 3. Collection Customization 🎛️
Using `@RepositoryRestResource`, we can change the path (e.g., from `scenario113Employees` to `employees`) and the relation name.

---

## 🏗️ Technical Details
- **Base Path**: `/data-api` (Configured in `application.properties`).
- **Media Type**: `application/hal+json` (HAL - Hypertext Application Language).

---

## 🧪 How to Test

### 1. Discover the API
```bash
curl http://localhost:8080/debug-application/data-api
```

### 2. Create an Employee (Standard POST)
```bash
curl -X POST "http://localhost:8080/debug-application/data-api/employees" \
     -H "Content-Type: application/json" \
     -d '{"name": "Alice Smith", "position": "Senior Developer", "salary": 120000.0}'
```

### 3. View Full Data (Incl. Salary)
```bash
curl http://localhost:8080/debug-application/data-api/employees/1
```

### 4. View Projected Data (HIDE Salary)
```bash
curl "http://localhost:8080/debug-application/data-api/employees/1?projection=summary"
```

---

## Interview Tip 💡

**Q**: *"What is Spring Data REST and when should you avoid it?"*  
**A**: *"Spring Data REST automatically exports repositories to REST. It is great for rapid prototyping or simple internal CRUD services. However, you should avoid it for complex business logic APIs where you need heavy validation, orchestration, or when the database model doesn't map 1:1 to the API representation."*

**Q**: *"How do you hide a sensitive field in Spring Data REST?"*  
**A**: *"You can use either `@JsonIgnore` on the entity field (permanent) or use **Projections** to create restricted views of the data dynamically based on a query parameter."*
