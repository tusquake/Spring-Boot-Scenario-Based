# Scenario 86: URL Data Extraction (@RequestParam vs @PathVariable)

## Overview
Modern REST APIs rely on URLs to pass data. Spring MVC provides two primary tools for this: **Path Variables** for identifying resources and **Request Parameters** for filtering and sorting.

---

## 🗺️ Visual Analogy: The City Map

![Path vs Query Analogy](file:///C:/Users/tushar.seth/.gemini/antigravity/brain/ae414fa8-11be-4ae0-99dc-266f09219526/path_variable_vs_request_param_analogy_1774093209660.png)

1.  **Street Address (`@PathVariable`)**: `/Street/105`. It points to a **fixed location**. If you change it, you are at a different house.
2.  **Store Filters (`@RequestParam`)**: `/Store/Fruits?color=red&size=large`. You are at the same store, but you are **filtering** what you see.

---

## 🛠️ Implementation Cheat Sheet

### 1. Identify a Resource (`@PathVariable`)
Used to point to a specific object.
- **Classic**: `/users/{id}`
- **Multiple**: `/catalogs/{cat}/items/{id}`
- **Optional**: `/details` or `/details/{type}` (using `Optional<T>`)
- **Security (Regex)**: `/{code:[0-9]{3}}` (Only allows 3-digit numbers).

### 2. Search & Filter (`@RequestParam`)
Used for dynamic data.
- **Required**: `?query=java` (Fails 400 if missing).
- **Optional**: `?sort=price` (Returns null if missing).
- **Default Value**: `?page=2` (Defaults to 0 if missing).
- **Multi-Value**: `?tags=java,spring` (Maps to `List<String>`).
- **Dynamic Map**: `?anyKey=anyVal` (Captures everything into a `Map`).

---

## 💡 Comparison Table

| Feature | @PathVariable | @RequestParam |
| :--- | :--- | :--- |
| **URL Placement** | Part of the path (`/users/1`) | Query String (`?id=1`) |
| **Primary Use** | Identifying specific resources. | Filtering, Sorting, Pagination. |
| **REST Style** | Hierarchical & Static. | Dynamic & Optional. |
| **Missing Value** | Results in 404 (Resource not found). | Results in 400 (Bad Request) unless optional. |

---

## Testing the Scenario
Try these `curl` commands:

- **Specific User**: `curl http://localhost:8080/debug-application/api/scenario86/users/105`
- **Regex Check (Valid)**: `curl http://localhost:8080/debug-application/api/scenario86/codes/123`
- **Regex Check (Invalid)**: `curl http://localhost:8080/debug-application/api/scenario86/codes/abc` (404)
- **Multi-tags**: `curl "http://localhost:8080/debug-application/api/scenario86/tags?values=java,spring"`
- **Search**: `curl "http://localhost:8080/debug-application/api/scenario86/search?query=spring&sort=asc"`
