# Scenario 110: Jackson Mapping & Annotations 🏆

## Overview
How do you control the structure of your REST API without polluting your database entities? What if the frontend needs `snake_case` but your Java code must follow `camelCase`?

**Scenario 110** demonstrates the professional use of **Jackson Annotations** to decouple your internal Java models from your external API contract. This solves several common anti-patterns related to hardcoded JSON and leaky data.

---

## 🚀 Key Annotations Demonstrated

### 1. The "@JsonProperty" (The Translator) 🗣️
This is the most common annotation. It tells Jackson: *"In Java, we call this `fullName`, but in the JSON API, it must be `full_name`."* This allows you to follow Java naming conventions while meeting external API standards.

### 2. The "@JsonIgnore" (The Security Guard) 👮‍♂️
Use this on sensitive fields (like `internalSecret`) to ensure they are **never** included in the JSON response, even if the object is serialized.

### 3. The "@JsonInclude(Include.NON_NULL)" (The Cleaner) 🧹
Tells Jackson to skip any field that is `null`. This keeps your API responses small, clean, and avoids "null-clutter" for optional fields.

### 4. The "@JsonFormat" (The Standardizer) ⌚
Defines exactly how dates or numbers should look in the JSON (e.g., `yyyy-MM-dd HH:mm:ss`).

---

## 🏗️ Implementation Details

### 1. The Annotated Model (`Scenario110JacksonModel.java`)
```java
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "full_name", "email_address", "created_at" })
public class Scenario110JacksonModel {
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email_address")
    private String email;

    @JsonIgnore
    private String internalSecret;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
```

---

## 🆚 Jackson vs. Hardcoded Strings

A common anti-pattern is building JSON strings manually. Jackson provides a much safer alternative:

| Feature | Hardcoded JSON Strings | Jackson Annotations |
| :--- | :--- | :--- |
| **Type Safety** | ❌ None. Typos lead to broken JSON. | ✅ High. The compiler catch errors. |
| **Maintenance** | ⚠️ Changes require updating string templates. | ✅ Easy. Update the DTO field or annotation. |
| **Security** | ❌ Risk of accidental data leakage. | ✅ Strong. `@JsonIgnore` blocks sensitive fields. |
| **Format** | 📉 Difficult to handle dates/nested objects. | 📈 Native support for complex types. |

---

## 🧪 How to Test

### 1. Verify Serialization (GET)
Fetch a sample object and observe the `snake_case` mapping and date formatting:
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario110/jackson"
```
**Expected Response Snippet:**
```json
{
  "id": 1,
  "full_name": "John Doe",
  "email_address": "john.doe@example.com",
  "created_at": "2024-04-07 06:30:40"
}
```

### 2. Verify Deserialization (POST)
Send a JSON with `snake_case` keys and verify that Jackson correctly maps them back to the Java DTO:
```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario110/jackson" \
     -H "Content-Type: application/json" \
     -d '{"full_name": "Jane Watson", "email_address": "jane@example.com"}'
```

---

## Interview Tip 💡

**Q**: *"How do you map a camelCase field to a snake_case key in a Spring Boot REST API?"*  
**A**: *"We use the `@JsonProperty("field_name")` annotation on the DTO field. Alternatively, we can configure a global naming strategy in `application.properties` using `spring.jackson.property-naming-strategy=SNAKE_CASE`."*

**Q**: *"When would you use @JsonIgnore instead of @JsonProperty(access = Access.WRITE_ONLY)?"*  
**A**: *"Use `@JsonIgnore` when a field should NEVER be part of the API (internal secret). Use `access = Access.WRITE_ONLY` when you want to accept a field from the user (like a password) but never send it back in the response."*
