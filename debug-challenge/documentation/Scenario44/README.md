# Scenario 44: Request Parameter Mastery

Demonstrates the flexible way Spring Boot handles search queries and optional inputs.

## Concept
REST APIs often need to filter data. `@RequestParam` is the standard way to capture `?key=value` pairs from the URL.

## Implementation Details
We demonstrated how to use `defaultValue` and determine if a parameter is `required`.

### Controller Snippet:
```java
@GetMapping("/search")
public String search(
        @RequestParam(defaultValue = "everything") String q,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(required = false) Integer limit) { ... }
```

## Verification Results
- **Empty**: `/api/scenario44/search` -> Returns "Searching for 'everything' [Page: 1, Limit: 10]".
- **Explicit**: `/api/search?q=pizza&page=5` -> Correctly captures and uses the specific values.

## Interview Theory: Parameters vs Paths
- **Rule of Thumb**: 
  - Use **@PathVariable** for unique resources (e.g., `/user/123`).
  - Use **@RequestParam** for filtering, sorting, or optional settings (e.g., `/users?role=admin`).
- **Lists**: You can also receive multiple values like `?id=1&id=2` into a `List<Long> ids`.
- **Security**: Remind the interviewer that all user inputs must be treated as untrusted and should be validated (e.g., set a `max` for the `limit` parameter to prevent heavy DB queries).
