# Scenario 03: Dependency Injection Patterns

## Overview
Dependency Injection (DI) is the core of the Spring Framework. While Spring supports several ways to inject dependencies, not all are created equal. This scenario compares **Constructor**, **Field**, and **Setter** injection.

---

## 📊 Comparison Table

| Feature | Constructor Injection | Field Injection | Setter Injection |
| :--- | :--- | :--- | :--- |
| **Immutability** | ✅ Yes (`final` fields) | ❌ No | ❌ No |
| **Null Safety** | ✅ Yes (checked at startup) | ❌ No | ❌ No |
| **Unit Testing** | ✅ Easy (no Spring needed) | ❌ Hard (needs reflection) | ⚠️ Moderate |
| **Circular Dependency** | ✅ Detected at startup | ❌ Hidden until runtime | ❌ Hidden until runtime |
| **Verdict** | **🏆 Recommended** | **🚮 Avoid** | **🔧 Use for Optional** |

---

## 🛠️ Why Constructor Injection Wins

1.  **Immutability**: By using `final` fields, you ensure that the dependency cannot be changed once the bean is initialized. This makes your code thread-safe and robust.
2.  **Testability**: You can simply call `new MyService(mockRepo)` in a JUnit test. With Field injection, you are forced to use `@Mock` and `@InjectMocks` (which uses reflection) or start a full Spring context, making tests slow.
3.  **Mandatory Dependencies**: It's impossible to create the object without providing the dependency. With setters or fields, the object can exist in a "partially initialized" state, leading to `NullPointerException` later.

---

## 🧪 Testing the Scenario
Use this `curl` command to verify all three patterns are working:

```bash
curl http://localhost:8080/debug-application/api/scenario3/test
```

### Expected Output:
```json
{
  "constructor_injection": "Constructor Service -> SUCCESS: Dependency Injected!",
  "field_injection": "Field Service -> SUCCESS: Dependency Injected!",
  "setter_injection": "Setter Service -> SUCCESS: Dependency Injected!",
  "verdict": "All work technically, but Constructor Injection is the ONLY one that allows 'final' fields and is easiest to unit test."
}
```

---

## Interview Tip 💡
If an interviewer asks: *"Why is Field Injection bad?"*
**Answer**: *"It couples your code too tightly to the DI container. You cannot instantiate the class in a plain unit test without using reflection or a Spring runner. It also prevents the use of 'final' keywords, making the class mutable and less robust."*
