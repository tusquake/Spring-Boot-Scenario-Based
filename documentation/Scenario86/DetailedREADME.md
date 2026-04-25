# PathVariable vs RequestParam — Complete Interview Reference

## Table of Contents
1. [Introduction to Request Mapping](#1-introduction)
2. [What is @PathVariable?](#2-path-variable)
3. [What is @RequestParam?](#3-request-param)
4. [Key Differences Table](#4-differences-table)
5. [The Classic Interview Trap: Missing Parameters (400 vs 404)](#5-the-classic-interview-trap-errors)
6. [Optional Parameters & Default Values](#6-optional-default)
7. [Regex Validation in @PathVariable](#7-regex-validation)
8. [Mapping to Lists and Maps](#8-lists-and-maps)
9. [Handling Matrix Variables](#9-matrix-variables)
10. [REST Best Practices: When to use which?](#10-best-practices)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Spring MVC provides several ways to extract data from an incoming HTTP request. The two most common methods are using the URL path itself and using query parameters.

---

## 2. @PathVariable
Extracts values from the URI template. It is used to identify a specific resource.
- **Example**: `/users/{id}`
- **Usage**: Identifying a unique object (User 105, Order ABC).

---

## 3. @RequestParam
Extracts values from the query string (the part after the `?`). It is used to filter, sort, or paginate resources.
- **Example**: `/users?role=admin&page=1`
- **Usage**: Filtering a list of resources.

---

## 4. Key Differences
| Feature | @PathVariable | @RequestParam |
|---|---|---|
| **Location** | URI Path | Query String |
| **Purpose** | Resource Identification | Filtering/Options |
| **Default** | Required | Required |
| **Default Value** | Not Supported | Supported |
| **Example** | `/users/10` | `/users?id=10` |

---

## 5. The Classic Interview Trap: Errors
**The Trap**: A user calls `/users/` instead of `/users/10`.
**The Result**: If the path is defined as `@GetMapping("/users/{id}")`, the browser will receive a **404 Not Found** because the URL doesn't match any handler.
**The Trap**: A user calls `/search` instead of `/search?q=java`.
**The Result**: If defined as `@RequestParam String q`, the browser will receive a **400 Bad Request** because the required parameter is missing.

---

## 6. Optional Parameters
- **PathVariable**: To make it optional, you must define multiple URL mappings or use `Optional<T>`.
- **RequestParam**: Simply use `required = false` or provide a `defaultValue`.

---

## 7. Regex Validation
You can add regex directly into the path mapping to restrict input:
`@GetMapping("/users/{id:[0-9]+}")`
This ensures that the method is only called if the ID is numeric. If the user passes characters, they get a 404.

---

## 8. Lists and Maps
- **List**: `@RequestParam List<String> tags` allows `/tags?values=a,b,c`.
- **Map**: `@RequestParam Map<String, String> allParams` captures every query parameter into a key-value map.

---

## 9. Matrix Variables
Matrix variables are similar to path variables but use a `;` separator (e.g., `/cars/color=red;year=2012`). They are rarely used in modern REST APIs but are supported by Spring if explicitly enabled.

---

## 10. REST Best Practices
- Use **PathVariable** for mandatory data that identifies the resource.
- Use **RequestParam** for optional data, filters, sorting, and pagination.
- **Bad URL**: `/users/search/byRole/admin` (Too complex for path).
- **Good URL**: `/users/search?role=admin`

---

## 11. Common Mistakes
1. Using `@PathVariable` for optional filters (makes the URL structure messy).
2. Forgetting that `@RequestParam` is required by default (leads to 400 errors).
3. Not using `defaultValue` for pagination parameters, forcing the client to always send them.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I have multiple @PathVariables in one method?**  
A: Yes, as long as the names match the placeholders in the `@RequestMapping`.  
**Q: Does @RequestParam work with POST requests?**  
A: Yes, it can extract data from both the query string AND from `application/x-www-form-urlencoded` form data in the request body.
