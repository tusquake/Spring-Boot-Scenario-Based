# API Versioning Strategies — Complete Interview Reference

## Table of Contents
1. [Why Version an API?](#1-why-version-api)
2. [URL Path Versioning (/v1/...)](#2-url-path)
3. [Request Parameter Versioning (?version=1)](#3-request-parameter)
4. [Custom Header Versioning (X-API-VERSION)](#4-custom-header)
5. [Media Type / Content Negotiation Versioning](#5-media-type)
6. [Comparison: Which Strategy to Choose?](#6-comparison)
7. [The Classic Interview Trap: URI Pollution vs Content Negotiation](#7-the-classic-interview-trap-trap)
8. [Supporting Backward Compatibility](#8-backward-compatibility)
9. [Deprecation Strategies (Sunset Header)](#9-deprecation)
10. [Versioning in Microservices (Gateway role)](#10-microservices)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Why Version an API?
APIs must evolve. As you add new features or change data structures, you might break existing clients. Versioning allows you to introduce "breaking changes" while still supporting older clients.

---

## 2. URL Path Versioning
The version is part of the URL.
```text
GET /api/v1/users
GET /api/v2/users
```
- **Pros**: Very visible, easy to cache by proxies.
- **Cons**: "Pollutes" the URI space (same resource has multiple URLs).

---

## 3. Request Parameter Versioning
The version is a query parameter.
```text
GET /api/users?version=1
```
- **Pros**: Simple to implement.
- **Cons**: Less standard; some caches ignore query parameters.

---

## 4. Custom Header Versioning
Clients send the version in a custom header.
```text
GET /api/users
X-API-VERSION: 2
```
- **Pros**: Keeps URLs clean (the "RESTful" way).
- **Cons**: Harder to test in a browser without plugins.

---

## 5. Media Type (Content Negotiation) Versioning
The version is inside the `Accept` header.
```text
GET /api/users
Accept: application/vnd.company.v2+json
```
- **Pros**: Theoretically the "most RESTful" because the version describes the *representation* of the data, not the resource itself.
- **Cons**: High complexity; not widely understood by developers.

---

## 6. Comparison Table
| Strategy | Implementation Ease | Browser Friendly | Caching Friendly |
|---|---|---|---|
| **URL Path** | High | Yes | High |
| **Headers** | Medium | No | Medium |
| **Media Type** | Low | No | Low |

---

## 7. The Classic Interview Trap: HATEOAS and Versioning
**The Question**: *"If an API is truly RESTful, do we even need versioning?"*
**The Answer**: Theoretically, with HATEOAS (Hypermedia as the Engine of Application State), clients follow links provided by the server. If a change happens, the server just provides new links. However, in the real world, data structure changes (renaming fields) still require versioning.

---

## 8. Supporting Backward Compatibility
Always try to add fields, not remove them. Use `@JsonProperty` aliases to keep old field names working while transitioning to new ones.

---

## 9. Deprecation Strategies
When a version is old, mark it as deprecated in the documentation and return a `Sunset` HTTP header to inform clients when it will be officially removed.

---

## 10. Versioning in Microservices
An API Gateway can handle versioning by routing `/v1/` requests to Service A and `/v2/` requests to Service B.

---

## 11. Common Mistakes
1. Not having a versioning strategy from day one.
2. Changing the version for minor, non-breaking additions (only change for breaking changes).
3. Maintaining too many old versions (creates technical debt).

---

## 12. Quick-Fire Interview Q&A
**Q: What is Semantic Versioning (SemVer)?**  
A: Major.Minor.Patch (e.g., 2.1.3). APIs usually only version the "Major" number.  
**Q: Which versioning strategy does GitHub use?**  
A: Custom Headers (Accept header).  
**Q: Which strategy does Stripe use?**  
A: Custom headers (specifically based on a date, e.g., `Stripe-Version: 2023-10-16`).
