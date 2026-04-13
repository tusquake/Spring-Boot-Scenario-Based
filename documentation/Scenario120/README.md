# Scenario 120: Service Layer Testing (Mockito Best Practices)

Service layer testing focuses on business logic in isolation. We use **Mockito** to mock dependencies (like repositories) and verify how the service interacts with them.

## 🚀 Mockito Best Practices

### 1. Annotations
- `@ExtendWith(MockitoExtension.class)`: Initializes mocks automatically.
- `@Mock`: Creates a mock instance of a dependency.
- `@InjectMocks`: Creates the service instance and injects the mocks into it.

### 2. BDD Style (Behavior Driven Development)
Using `BDDMockito` makes tests more readable by following the **Given-When-Then** pattern:
```java
// Given
given(repository.findById(id)).willReturn(Optional.of(user));

// When
service.doSomething(id);

// Then
then(repository).should().save(any());
```

### 3. Argument Captors
When you need to verify the *internal state* of an object passed to a dependency:
```java
ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
then(repository).should().save(captor.capture());
assertEquals("EXPECTED_STATUS", captor.getValue().getStatus());
```

### 4. Verifying Interactions
- `then(mock).should(times(n)).method()`: Verify number of calls.
- `then(mock).shouldHaveNoInteractions()`: Ensure no calls were made (e.g., after validation failure).

---

## 🛠️ Implementation Details

### Testing State Transitions
The `deactivateUser` method updates a user's status. Since the repository `save()` usually returns the same object, we use `ArgumentCaptor` to verify that the status was changed to `INACTIVE` *before* the save call.

---

## 🚀 How to Run

Run the test:
```bash
mvn test -Dtest=Scenario120ServiceTest
```

## 📝 Interview Tip
> "When should you use @Spy instead of @Mock?"
>
> Use `@Mock` for full isolation (complete replacement). Use `@Spy` when you want to call real methods on the object but still track interactions or stub specific methods (partial mocking). Generally, favor `@Mock` for service dependencies.
