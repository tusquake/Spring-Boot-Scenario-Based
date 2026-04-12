# Scenario 118: Unit vs Integration Testing (@SpringBootTest vs @WebMvcTest)

In Spring Boot, choosing the right testing strategy is crucial for balance between **Speed** and **Accuracy**. This scenario demonstrates the three primary layers of testing.

## 🧪 Testing Strategies

| Feature | Unit Test (Mockito) | Sliced Integration (@WebMvcTest) | Full Integration (@SpringBootTest) |
| :--- | :--- | :--- | :--- |
| **Startup Time** | Fast (Milliseconds) | Moderate (Seconds) | Slow (Seconds/Minutes) |
| **Spring Context** | None (POJO) | Minimal (Web Layer only) | Full Application Context |
| **Dependencies** | Mocked manually | `@MockBean` / Mocked | Real Beans (or `@MockBean`) |
| **Use Case** | Business Logic, Math, Rules | Controller logic, Serialization | End-to-End flow, DB interaction |

---

## 🛠️ Implementation Details

### 1. Unit Test: `UserServiceUnitTest.java`
- Uses `@ExtendWith(MockitoExtension.class)`.
- No Spring Context is loaded.
- Mockito mocks the dependency (`UserRepository`).
- **Goal**: Verify if the `createUser` method correctly handles existing emails.

### 2. Sliced Integration Test: `UserControllerWebMvcTest.java`
- Uses `@WebMvcTest(Scenario118Controller.class)`.
- Loads only the controller and its infrastructure (Jackson, Security).
- Mocks the Service layer using `@MockBean`.
- **Goal**: Verify URI mapping, JSON serialization, and status codes.

### 3. Full Integration Test: `UserIntegrationTest.java`
- Uses `@SpringBootTest`.
- Loads the entire application context.
- Uses `TestRestTemplate` to make real HTTP calls to a random port.
- Interacts with a real (H2) database.
- **Goal**: Verify that the entire flow from Controller -> Service -> Repository works correctly.

---

## 🚀 How to Run

1. Run all tests for this scenario:
   ```bash
   mvn test -Dtest=com.interview.debug.scenario118.*
   ```

2. Observe the console logs to see the Spring Context loading (or not loading) for each test type.

## 📝 Interview Tip
> "When should you use `@SpringBootTest`?"
>
> Use it sparingly. It's the most reliable but slowest test. Prefer `@WebMvcTest` for API testing and pure Unit Tests for service/logic layers to keep your CI/CD pipeline fast.
