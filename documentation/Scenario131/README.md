# Scenario 131: Architecture Enforcement (ArchUnit)

This scenario demonstrates how to use **ArchUnit** to enforce architectural rules and maintain a clean project structure.

## Concept
In large projects, it's common for code to "leak" between layers. For example:
- A Controller bypassing the Service layer and talking directly to the Repository.
- A Service depending on web-specific classes (like `HttpServletRequest`).
- DTOs being placed in the Service package incorrectly.

**ArchUnit** allows you to write unit tests that fail if these rules are broken, ensuring the architect's vision is maintained automatically.

## Implementation Details

### Maven Profile (`arch-check`)
To avoid breaking current builds, we added the dependency inside a Maven profile. This ensures ArchUnit is only processed when requested.

### Rule Defined in `ArchitectureTest.java`
1.  **Layer Isolation**: Controllers must not depend on classes with names ending in `Repository`.
2.  **Package Residency**: Classes with names ending in `Service` or `ServiceImpl` must reside in a `..service..` package.

### Intentional Violation
`ViolationController.java` intentionally injects `BankAccountRepository` directly. This is a common anti-pattern that couples the web layer directly to the database layer.

## How to Run

Since we put this behind a profile to keep it "disabled" by default, you must activate the profile to see the tests run:

```bash
mvn test -Parch-check -Dtest=ArchitectureTest
```

### Expected Result
The test `controllersShouldNotDependOnRepositories` will **FAIL** because of `ViolationController`.

## How to Fix
To fix the violation:
1.  Create a `BankAccountService`.
2.  Move the repository dependency into the service.
3.  Inject the service into `ViolationController`.

## Interview Theory: Architectural Integrity
- **Why use ArchUnit instead of just code reviews?** Automation is faster and less prone to human error. It provides immediate feedback to developers.
- **Onion vs. Hexagonal Architecture**: Tools like ArchUnit are essentials for enforcing "Inner" and "Outer" ring dependency rules.
- **Technical Debt**: ArchUnit prevents the accumulation of "shortcut code" that eventually makes a system unmaintainable.
