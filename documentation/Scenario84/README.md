# Scenario 84: Spring MVC Interceptors

## Overview
**Interceptors** allow you to intercept HTTP requests and responses at the Spring MVC level. They are perfect for "cross-cutting concerns" that require access to the Spring Context.

---

## 🗺️ The 7-Layer Request Journey

Where does the Interceptor sit? It sits at **Layer 4**, inside the Spring Context, just before the Controller.

![Spring Boot 7 Layer Journey](file:///C:/Users/tushar.seth/.gemini/antigravity/brain/ae414fa8-11be-4ae0-99dc-266f09219526/spring_boot_7_layer_journey_v2_1774086855652.png)

---

## 🛡️ Analogy: The Building Security Check-post

Imagine an office building:
1.  **preHandle**: The security guard checks your ID at the front desk.
2.  **Controller**: You enter the meeting room and do your work.
3.  **postHandle**: On your way out, you return your visitor badge.
4.  **afterCompletion**: The janitor cleans the room after everyone has left.

---

## The 3 Lifecycle Methods 🔄

### 1. preHandle()
-   Executed **before** the controller method.
-   Returns `boolean`. If `false`, the request is blocked and the controller is never called.

### 2. postHandle()
-   Executed **after** the controller method but **before** the view is rendered.
-   You can modify the `ModelAndView` here.

### 3. afterCompletion()
-   Executed after the entire request finished (including view rendering).
-   Great for resource cleanup or performance timing.

---

## 🌍 Real-World Use Cases (Professional Grade)

1.  **Maintenance Window**: Block POST/PUT requests during DB upgrades while keeping GET requests open.
2.  **Performance Auditing**: Start a timer in `preHandle` and stop it in `afterCompletion` to find slow APIs.
3.  **API Versioning**: Check headers and add a `X-API-Deprecated: true` warning if the version is old.
4.  **Context Decoration**: Read a `X-Tenant-ID` header once and add it as a request attribute so all controllers can see it.
5.  **Internationalization**: Change the app language based on a `?lang=en` query parameter.

---

## Interview Tip: Interceptor vs. Filter 💡

| Feature | Filter (Servlet) | Interceptor (Spring MVC) |
| :--- | :--- | :--- |
| **Scope** | Low-level (Servlet container) | High-level (Spring context) |
| **Access** | No access to Spring beans easily. | Full access to all Spring beans. |
| **Logic** | Operates on Request/Response streams. | Operates on Handler/ModelAndView. |
| **Use Case** | Encryption, Compression, CORS, JWT. | Logging, Auth, UI Logic. |
