# Advanced Mockito Techniques — Complete Interview Reference

## Table of Contents
1. [Introduction to Mockito](#1-introduction)
2. [Given-When-Then: BDD Style Testing](#2-bdd-style)
3. [Argument Captors: Inspecting Hidden State](#3-argument-captors)
4. [Argument Matchers (any(), eq())](#4-argument-matchers)
5. [The Classic Interview Trap: Mocking Final Classes and Static Methods](#5-the-classic-interview-trap-trap)
6. [Spying: Partial Mocking](#6-spying)
7. [Verifying Method Call Order](#7-verification-order)
8. [Handling Consecutive Calls (stubbing)](#8-consecutive-calls)
9. [Mockito with JUnit 5 (@ExtendWith)](#9-junit5-integration)
10. [Mocking Void Methods](#10-void-methods)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Mockito is the most popular mocking framework for Java. It allows you to create mock objects and define their behavior, which is essential for writing isolated unit tests.

---

## 2. BDD Style Testing
Spring and Mockito encourage the BDD (Behavior Driven Development) style:
- `given(repo.find(1L)).willReturn(user)`
- `when(service.process(1L))`
- `then(repo).should().save(any())`
This makes tests read like natural language.

---

## 3. Argument Captors
Sometimes you want to verify not just that a method was called, but **what exactly** was passed to it (especially for internally created objects).
```java
ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
verify(repo).save(captor.capture());
assertEquals("INACTIVE", captor.getValue().getStatus());
```

---

## 4. Argument Matchers
You can use matchers like `anyLong()`, `anyString()`, or `eq(expectedValue)` when you don't care about the exact value or when only some values matter.
**Rule**: If you use a matcher for one argument, you **MUST** use matchers for all arguments in that method call.

---

## 5. The Classic Interview Trap: Static Methods
**The Trap**: How do you mock a static method like `LocalDateTime.now()`?
**The Problem**: Older versions of Mockito couldn't do this. Developers had to use PowerMock, which was buggy and slow.
**The Fix**: Since Mockito 3.4.0, you can use `mockStatic`:
```java
try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
    mocked.when(LocalDateTime::now).thenReturn(fixedDate);
}
```

---

## 6. Spying
A `@Spy` wraps a real object. It calls the real methods unless you explicitly stub them. This is useful for testing "legacy" code where you want to mock one method but let others run normally.

---

## 7. Verification Order
If you need to ensure that Method A is called before Method B:
`InOrder inOrder = inOrder(mockA, mockB);`
`inOrder.verify(mockA).doA();`
`inOrder.verify(mockB).doB();`

---

## 8. Consecutive Calls
You can simulate different behaviors for repeated calls to the same method:
`given(mock.call()).willReturn("A").willReturn("B").willThrow(new RuntimeException());`

---

## 9. JUnit 5 Integration
Use `@ExtendWith(MockitoExtension.class)` on the test class. This automatically initializes all fields marked with `@Mock`, `@Spy`, and `@InjectMocks`.

---

## 10. Void Methods
To stub a method that returns `void` (like `delete`), you cannot use `given(...)`. You must use the "do" family of methods:
`doThrow(new RuntimeException()).when(mock).delete(anyLong());`

---

## 11. Common Mistakes
1. Not resetting mocks between tests (rarely needed with JUnit 5).
2. Using `@InjectMocks` on an interface (it must be a concrete class).
3. Mocking everything (Don't mock Data Objects/DTOs; just instantiate them).

---

## 12. Quick-Fire Interview Q&A
**Q: What is the difference between @Mock and @InjectMocks?**  
A: `@Mock` creates the mock object. `@InjectMocks` creates a real instance of your service and injects the created mocks into it.  
**Q: Can Mockito mock private methods?**  
A: No. Mockito cannot mock private methods. If you feel the need to do so, your class likely needs refactoring.
