# Scenario 85: Master the Types of Filters

## Overview
In Spring Boot, **Filters** are the first line of defense. They sit at the Servlet level (Layer 2) and intercept requests before they even reach the Spring DispatcherServlet.

---

## 🛫 Analogy: Airport Security Checkpoint

Imagine you are arriving at an airport for a flight:
1.  **Standard Filter (The Main Gate)**: You enter the building. Your bags are scanned. This happens every time you enter.
2.  **OncePerRequestFilter (Passport Control)**: Your identity is verified. In a complex airport with multiple terminals (internal forwards), you only want to show your passport **once**.
3.  **Controller (The Flight)**: You finally board the plane and reach your destination.

![Airport Security Analogy](file:///C:/Users/tushar.seth/.gemini/antigravity/brain/ae414fa8-11be-4ae0-99dc-266f09219526/spring_filter_types_analogy_1774090295647.png)

---

## The Two Main Types of Filters 🛠️

### 1. Standard Servlet Filter (`jakarta.servlet.Filter`)
-   The lowest level.
-   Part of the Servlet API, not Spring-specific.
-   **Risks**: Can be executed multiple times per request if there are internal forwards or includes.

### 2. Spring's `OncePerRequestFilter`
-   A Spring-specific base class.
-   **Guarantee**: It is executed exactly **once** per request, regardless of internal dispatching.
-   **Best Practice**: Use this for JWT validation, authentication, and any logic where "double processing" would be a bug.

---

## How to Register Filters? ⚙️

There are two main ways to register filters in Spring Boot:

### A. `@Component` (The Simple Way)
-   Just add `@Component` to your filter class.
-   **Downside**: It applies to **ALL** URL patterns and you have less control over the order.

### B. `FilterRegistrationBean` (The Professional Way)
-   Used in a `@Configuration` class.
-   **Benefits**:
    -   Can specify exact **URL Patterns** (e.g., only `/api/**`).
    -   Can set a specific **Order** (e.g., Filter A must run before Filter B).
    -   Can give the filter a custom name for monitoring.

---

## Testing the Scenario
1. **Hit the Endpoint**:
   `GET /api/scenario85/test`
2. **Check the Console**:
   You will see the interleaved logs showing the exact sequence:
   ```text
   [Scenario 85] Standard Filter: START...
   [Scenario 85] OncePerRequestFilter: START...
   [Scenario 85] Controller: Inside the secure room!
   [Scenario 85] OncePerRequestFilter: END...
   [Scenario 85] Standard Filter: END...
   ```
