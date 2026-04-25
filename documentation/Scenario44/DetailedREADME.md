# Request Parameters (@RequestParam) — Complete Interview Reference

## Table of Contents
1. [What is @RequestParam?](#1-what-is-request-param)
2. [@RequestParam vs @PathVariable](#2-requestparam-vs-pathvariable)
3. [Making Parameters Optional (required = false)](#3-optional-params)
4. [Using Default Values (defaultValue)](#4-default-values)
5. [The Classic Interview Trap: Missing Parameter Exception](#5-the-classic-interview-trap-missing)
6. [Mapping to Multiple Values (List/Array)](#6-multiple-values)
7. [Mapping All Params to a Map<String, String>](#7-mapping-to-map)
8. [Type Conversion and Custom Formatters](#8-type-conversion)
9. [Validation on Request Parameters (@Min, @Max)](#9-validation)
10. [Handling Multi-part Form Data](#10-multipart)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is @RequestParam?
`@RequestParam` is used to extract query parameters, form parameters, and even files from the request. Query parameters appear after the `?` in the URL (e.g., `/search?q=java`).

---

## 2. @RequestParam vs @PathVariable
- **@PathVariable**: Used for identifying a resource (e.g., `/users/123`).
- **@RequestParam**: Used for filtering, sorting, or pagination (e.g., `/users?role=admin&sort=name`).

---

## 3. Making Parameters Optional
By default, `@RequestParam` is required. If the parameter is missing, Spring throws a `MissingServletRequestParameterException`. You can make it optional using `required = false`.

---

## 4. Using Default Values
The `defaultValue` attribute allows you to provide a fallback if the parameter is missing. **Note**: Setting a `defaultValue` implicitly sets `required = false`.

---

## 5. The Classic Interview Trap: Empty vs Missing
**The Question**: *"What happens if you have @RequestParam(defaultValue = "test") and the user sends /search?q= (empty string)?"*
**The Answer**: The value will be an **EMPTY STRING**, not the default value. The default value only triggers if the parameter is completely absent from the URL.

---

## 6. Mapping to Multiple Values
If a user sends `/search?tags=java&tags=spring`, you can capture this as a `List<String> tags` or `String[] tags`.

---

## 7. Mapping All Params to a Map
If you don't know the parameter names in advance, you can use `Map<String, String>` to capture all query parameters as key-value pairs.

---

## 8. Type Conversion
Spring automatically converts the string parameter to the target type (int, long, boolean, etc.). You can also use `@DateTimeFormat` to parse date strings directly into `LocalDate`.

---

## 9. Validation on Parameters
You can use JSR-303 annotations on parameters if you add `@Validated` to your controller class:
```java
public String search(@Min(1) @RequestParam int page) { ... }
```

---

## 10. Handling Multi-part Form Data
`@RequestParam` is also used to handle file uploads when the content type is `multipart/form-data`.

---

## 11. Common Mistakes
1. Using `@RequestParam` when `@PathVariable` would be more RESTful for resource IDs.
2. Forgetting that parameters are required by default.
3. Not handling `NumberFormatException` when manual conversion is attempted instead of letting Spring handle it.

---

## 12. Quick-Fire Interview Q&A
**Q: Can @RequestParam be used with POST requests?**  
A: Yes, it can read parameters from the query string of a POST URL or from a URL-encoded form body.  
**Q: How do you handle a list of integers?**  
A: Just use `List<Integer>` as the parameter type; Spring will handle the conversion.
