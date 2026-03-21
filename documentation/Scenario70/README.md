# Scenario 70: API Documentation (Swagger/OpenAPI)

In modern microservices, **API Documentation** is not optional—it's the "contract" between services and developers. **Swagger (OpenAPI)** allows you to generate this documentation automatically from your code.

---

## 🛠️ Why use OpenAPI?
1. **Interactive UI**: Test APIs directly from the browser without `curl` or Postman.
2. **Standardization**: Uses the industry-standard OpenAPI 3.0 specification.
3. **Client Generation**: Can be used to generate client code in Python, JS, Java, etc.

---

## 🚀 How to Access
After starting the application with the new context path `/debug-application`:

1. **Swagger UI**:
   [http://localhost:8080/debug-application/swagger-ui/index.html](http://localhost:8080/debug-application/swagger-ui/index.html)
2. **OpenAPI JSON**:
   [http://localhost:8080/debug-application/v3/api-docs](http://localhost:8080/debug-application/v3/api-docs)

---

## 📝 Key Annotations used in [Scenario70Controller](file:///c:\Users\tushar.seth\Desktop\Scenario%20Based\debug-challenge\src\main\java\com\interview\debug\controller\Scenario70Controller.java)
| Annotation | Purpose |
| :--- | :--- |
| `@Tag` | Groups endpoints together (e.g., "User Management"). |
| `@Operation` | Provides a summary and detailed description of the endpoint. |
| `@Parameter` | Documents request parameters, including examples. |
| `@ApiResponse` | Explains what different status codes (200, 404, 500) mean. |

---

## 💡 Interview Tips
- **Q: What is the difference between Swagger and OpenAPI?**
  - **A**: OpenAPI is the **specification** (the rules), while Swagger is a **set of tools** (like Swagger UI, Swagger Editor) that implement the specification.
- **Q: How do you secure Swagger in production?**
  - **A**: You should never expose Swagger in production! You can disable it using profiles or secure it behind Spring Security with specific role requirements.
- **Q: Can you generate code from Swagger?**
  - **A**: Yes, using tools like `openapi-generator`, you can generate SDKs for frontend or other microservices.
