# Scenario 115: @ModelAttribute Masterclass 📋

## Overview
What is the difference between `@ModelAttribute` and `@RequestBody`? 

While `@RequestBody` excels at JSON (REST APIs), **`@ModelAttribute`** is the powerhouse for **HTML Forms** and **URL Data Binding**.

---

## 🚀 Key Patterns

### 1. Method-Level @ModelAttribute (The "Data Populator")
If a method is annotated with `@ModelAttribute`, Spring calls it **BEFORE** every handler method in the same controller. 
- **Use Case**: Populating "Reference Data" (like a list of categories or countries) that should be available on the form page, regardless of which button the user clicks.
- **Example in Code**: Look at `Scenario115Controller.populateTopics()`.

### 2. Parameter-Level @ModelAttribute (The "Data Binder")
When used on a controller parameter, Spring maps the incoming request parameters (query string or form data) to a Java object using **Setter Injection**.
- **The "Magic"**: Spring looks at the names (e.g., `username`) and automatically calls `setUsername()` on the DTO.
- **Content-Type**: Works with `application/x-www-form-urlencoded`.

---

## 🏗️ Technical Details
- **Conversion**: Spring uses `DataBinder` and `PropertyEditors` (or `Converters`) to map strings to types like `int`, `boolean`, or `Date`.
- **Precedence**: Method-level attributes are added to the model FIRST.

---

## 🧪 How to Test

### 1. Inspect Pre-populated Model
Check if the "topics" list is automatically added to the model:
```bash
curl http://localhost:8080/debug-application/api/scenario115/form
```

### 2. Submit Form Data (Binding)
Send form-encoded data (NOT JSON) to see the binding:
```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario115/submit" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "username=JohnDoe&email=john@test.com&favoriteTopic=Spring+Boot"
```

---

## Interview Tip 💡

**Q**: *"When does @ModelAttribute get executed in the lifecycle?"*  
**A**: *"Method-level @ModelAttribute methods are executed **before** the actual @RequestMapping handler method of the controller. This allows you to 'prime' the model with data that your handler or view might need."*

**Q**: *"Can you use @ModelAttribute and @RequestBody together?"*  
**A**: *"Technically yes, but they serve different purposes. @RequestBody consumes the 'input stream' (JSON), while @ModelAttribute parses 'Request Parameters' (form fields). You cannot use both to parse the same input body."*
