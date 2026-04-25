# Scenario 19: API Version Lifecycle Management

## Overview
Managing the lifecycle of an API is critical for maintaining system stability while evolving your service. This scenario demonstrates how to transition an API version through different lifecycle phases to guide clients toward newer versions without abrupt breaking changes.

---

## 📅 Version Lifecycle Phases

| Phase | Duration | What Happens | HTTP Strategy |
| :--- | :--- | :--- | :--- |
| **Current** | Ongoing | Active development, full support. | `200 OK` |
| **Deprecated** | 3-6 months | No new features, security fixes only. | `200 OK` + `Deprecation` Header |
| **Sunset** | 1-3 months | Warning logs, migration reminders. | `200 OK` + `Sunset` Header |
| **Removed** | Final | Clients MUST migrate. | `410 Gone` |

---

## 🛠️ Implementation Details

### 1. Deprecation Header
We use the `Deprecation` HTTP header to inform clients that the version they are using is no longer the recommended one.
```http
Deprecation: Fri, 31 Dec 2025 23:59:59 GMT
```

### 2. Sunset Header
The `Sunset` header indicates when the API version will be completely taken offline.
```http
Sunset: Sat, 01 Mar 2026 23:59:59 GMT
```

### 3. 410 Gone
Once an API is removed, returning `410 Gone` is more descriptive than `404 Not Found`. It tells the client that the resource was here, but it is intentionally gone and will not return.

---

## 🧪 Testing the Scenario

Try these `curl` commands to see the lifecycle in action:

1. **Current Version (v2)**:
```bash
curl -i http://localhost:8080/debug-application/api/scenario19/v2/data
```
*Expect: 200 OK*

2. **Deprecated Version (v1)**:
```bash
curl -i http://localhost:8080/debug-application/api/scenario19/v1/deprecated
```
*Expect: 200 OK with `Deprecation` header*

3. **Sunset Version (v1)**:
```bash
curl -i http://localhost:8080/debug-application/api/scenario19/v1/sunset
```
*Expect: 200 OK with `Sunset` header and warning message*

4. **Removed Version (v1)**:
```bash
curl -i http://localhost:8080/debug-application/api/scenario19/v1/removed
```
*Expect: 410 Gone*

---

## Interview Tip 💡
**Q**: *"What is the difference between Deprecated and Sunset?"*
**A**: *"**Deprecation** is a declaration of intent. It tells users 'You should stop using this, we aren't adding features anymore.' **Sunset** is a deadline. It tells users 'This will stop working on X date.' Always provide a Sunset date after a Deprecation period to give clients time to migrate."*
