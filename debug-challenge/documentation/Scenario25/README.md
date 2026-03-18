# Scenario 25: API Versioning Strategies

Demonstrates the three most common ways to version REST APIs to maintain backward compatibility.

## Concept
As your API evolves, you can't break existing clients. You must provide a way for new clients to use new features while old clients stay on the old logic.

## Implementation Details
1. **URL Path**: `/api/v1/items` -> Easy to see, cache-friendly.
2. **Custom Header**: `X-API-VERSION: 2` -> Keeps URLs clean, but harder to test in browsers.
3. **Media Type (Accept Header)**: `Accept: application/vnd.company.v3+json` -> The "purest" REST approach.

### Implementation Snippet:
```java
@GetMapping(value = "/items", headers = "X-API-VERSION=2")
public Map<String, Object> getItemsV2() { ... }
```

## Verification Results
- **V1**: Hit `/v1/items`.
- **V2**: Hit `/items` with header `X-API-VERSION=2`.
- **V3**: Hit `/items` with header `Accept=application/vnd.company.v3+json`.
- **Result**: Each approach returns a different data structure (`name` vs `fullName`).

## Interview Theory: Versioning
- **Which is best?**: Most industry leaders (Google, Microsoft) use **URL versioning** (`/v1/`) because it's the simplest and works with standard caching.
- **Breaking Changes**: Only increment the version for breaking changes (e.g., deleting a field). For adding a field, usually no new version is needed.
- **Deprecation**: Mention that you should return a `Deprecation` header to inform clients they need to migrate.
