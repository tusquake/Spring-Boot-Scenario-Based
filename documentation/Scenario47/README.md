# Scenario 47: Advanced API Versioning (URI vs Header)

A deep dive into the implementation trade-offs of the two most common versioning strategies.

## Concept
1. **URI Path**: `/api/v1/product` vs `/api/v2/product`.
2. **Custom Header**: `/api/product` with `X-API-VERSION: 2`.

## Implementation Details
Using Spring's `headers` attribute in `@GetMapping` to route requests.

### Path Versioning:
```java
@GetMapping("/v2/product")
public Map<String, Object> getProductV2() { ... }
```

### Header Versioning:
```java
@GetMapping(value = "/product", headers = "X-API-VERSION=2")
public Map<String, Object> getProductHeaderV2() { ... }
```

## Verification Results
- **Test V1 Path**: `/api/v1/product` -> Returns Price: 5000.
- **Test V2 Path**: `/api/v2/product` -> Returns Price: 7500 + New Fields.
- **Test Header**: Same URL, change the header.

## Interview Theory: Versioning Strategy
- **Caching**: Path versioning is better for CDNs (Content Delivery Networks) because the URL is unique.
- **Complexity**: Header versioning is harder to document in tools like Swagger (OpenAPI) if not configured correctly.
- **Media Type Versioning**: Mention a third option: `Accept: application/vnd.company.app-v2+json`. This is often considered the most "Restful" but is rarely used due to complexity.
