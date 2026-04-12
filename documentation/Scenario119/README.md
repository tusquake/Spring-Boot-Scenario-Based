# Scenario 119: MockMvc Deep Dive (Controller Testing)

Testing controllers requires more than just calling a method. We need to verify the HTTP layer: status codes, serialization, headers, and parameters. `MockMvc` is the standard tool for this in Spring Boot.

## 🚀 Key MockMvc Techniques

### 1. Handling Request Components
- **Headers**: Use `.header("Key", "Value")` to simulate custom headers.
- **Query Parameters**: Use `.param("key", "value")` to simulate URL parameters (e.g., `?name=Tushar`).
- **Body**: Use `.content(jsonString)` along with `.contentType(MediaType.APPLICATION_JSON)`.

### 2. Validating Responses
- **Status Codes**: `.andExpect(status().isOk())`, `.isCreated()`, `.isBadRequest()`.
- **JSON Validation**: `.andExpect(jsonPath("$.field").value("expected"))`.
- **String Content**: `.andExpect(content().string("expectedBody"))`.

### 3. Debugging
- **Print**: use `.andDo(print())` to see the full request/response details in the console.

---

## 🛠️ Implementation Details

### Standalone vs. WebMvcTest
In this scenario, we use **Standalone Setup** for speed:
```java
mockMvc = MockMvcBuilders.standaloneSetup(new Scenario119Controller()).build();
```
This bypasses the Spring Context and Security, making it a pure Unit Test for the Controller logic.

### Complex JSON Validation
We use **JsonPath** to verify nested structures:
```java
mockMvc.perform(post("/api/scenario119/complex-json")
        .content(objectMapper.writeValueAsString(payload)))
    .andExpect(jsonPath("$.processed").value(true))
    .andExpect(jsonPath("$.timestamp").exists());
```

---

## 🚀 How to Run

1. Run the test:
   ```bash
   mvn test -Dtest=Scenario119ControllerTest
   ```

2. Watch for the `print()` output in the logs to see the generated JSON response.

## 📝 Interview Tip
> "How do you test validation errors (e.g., @Valid) in a controller?"
>
> Use `MockMvc` to send an invalid JSON body and expect `status().isBadRequest()`. If you use `@ControllerAdvice`, ensure it is registered in your `standaloneSetup` (via `.setControllerAdvice(...)`) or use `@WebMvcTest` which loads it automatically.
