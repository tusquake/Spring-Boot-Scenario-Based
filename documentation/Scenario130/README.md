# Scenario 130: Static Analysis (PMD & Checkstyle)

Demonstrates how to integrate **PMD** and **Checkstyle** into a Spring Boot project to enforce code quality and detect potential bugs.

## Concept
Static analysis tools inspect your source code without executing it. They find common issues like:
- **Checkstyle**: Formatting errors (tabs vs spaces), naming conventions, long methods, missing Javadoc.
- **PMD**: Bug patterns (unused variables, empty catch blocks), overly complex code, performance issues.

## Implementation Details

### Maven Integration
We added the following plugins to `pom.xml`:
1.  **maven-checkstyle-plugin**: Configured to use a custom ruleset in `config/checkstyle/checkstyle.xml`.
2.  **maven-pmd-plugin**: Configured to use a custom ruleset in `config/pmd/pmd-ruleset.xml`.

Both are tied to the `validate` or `verify` phase, meaning the build will fail if violations are found.

### Intentional Violations
See `StaticAnalysisController.java` for examples:
- `private String User_Name`: Checkstyle violation (Member Name).
- `int x=10;`: Checkstyle violation (Whitespace).
- `String unusedVar`: PMD violation (Unused Local Variable).
- Deeply nested `if` blocks: PMD violation (Cyclomatic Complexity).

## How to Run

### Checkstyle Only
```bash
mvn checkstyle:check
```

### PMD Only
```bash
mvn pmd:check
```

### Full Build (Fails on first violation)
```bash
mvn clean verify
```

## How to Fix
1.  **Checkstyle**: Standardize naming (camelCase), add spaces around operators, and ensure switch statements have a `default` case.
2.  **PMD**: Remove unused variables/methods, and refactor deeply nested logic into smaller functions.

## Interview Theory: Code Quality
- **Difference between Unit Testing and Static Analysis**: Unit testing checks for functional correctness; static analysis checks for structural and stylistic correctness.
- **CI/CD Integration**: Why run these in CI? To ensure no "dirty" code ever reaches the main branch.
- **Ruleset Customization**: How to handle a rule that doesn't fit your project? (e.g., use `@SuppressWarnings("CPD")` or modify the ruleset XML).
