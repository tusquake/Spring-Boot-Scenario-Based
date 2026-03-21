# Scenario 82: Spring Web Scopes

## Overview
By default, all beans in Spring are **Singletons** (one instance for the whole app). However, web applications often need beans that live only for as long as a user's request or their entire browser session.

## The 5 Major Scopes 🏗️

### 1. Singleton (Default)
Exactly one instance is created per Spring IoC container. 
- **Use Case**: Stateless services, Repositories, Controllers.

### 2. Prototype
A new instance is created **every time** the bean is requested (injected). 
- **Use Case**: Statefull beans, non-thread-safe objects.

### 3. Request
A new instance is created for each HTTP request. It is destroyed when the request is finished.
- **Use Case**: Storing request-specific metadata, user-agent info, or a unique Trace ID for that request.

### 4. Session
A new instance is created for each HTTP Session. It survives across multiple requests from the same user.
- **Use Case**: Shopping Carts, User Preferences, Login state.

### 5. Application
Exactly one instance per `ServletContext`. Similar to a singleton, but scoped to the web container level.

---

## 🏛️ Analogy: Singleton vs. Application Scope

Think of a large office building (the **Web Server**):
- **Singleton Scope**: Each private office suite has its **own** dedicated printer. If you have 3 suites, you have 3 separate printers.
- **Application Scope**: The **Main Building Reception Desk**. No matter how many private offices there are, they all share only one reception desk at the front door.

![Spring Scopes Analogy](https://www.baeldung.com/wp-content/gemini/gemini/spring_scopes_analogy_v2_1774081396883.png)

---

## 🛑 The "Scoped Proxy" Problem (Crucial for Interviews!)
If you try to inject a **Request-scoped** bean into a **Singleton** controller, you have a problem: The controller is created once at startup, but the request bean doesn't even exist yet!

To fix this, we use **Scoped Proxies**:
```java
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
```
- **How it works**: Spring injects a **Proxy** (a placeholder) into the controller. Every time the controller calls a method on that proxy, the proxy looks up the *actual* request-scoped bean for the current thread and forwards the call.

---

## Testing the Scenario
1. **First Access**: `GET /api/scenario82/scopes`
   - Note the `request_bean_id` and `session_bean_id`. 
   - `session_visit_count` will be 1.

2. **Refresh the Page**:
   - `request_bean_id` **will change** (new request).
   - `session_bean_id` **stays the same** (same user).
   - `session_visit_count` increments to 2.

3. **Open in Incognito / Different Browser**:
   - Both `request_bean_id` and `session_bean_id` **will be different**.
   - `session_visit_count` starts over at 1.
