# Scenario 96: Startup Environment Validation (env-validator)

## Overview
How do you ensure that your application doesn't start if it's missing critical configuration?

Hard-coding secrets is a security risk, but relying on environment variables that might be missing at runtime is an operational risk. **Scenario 96** demonstrates how to use the `env-validator` library to enforce a "Fail-Fast" architecture.

---

## 🚀 The library: `env-validator`

The `env-validator` library (created by @tusquake) provides a declarative way to validate environment variables during the Spring Boot startup sequence.

### Key Annotations:
1.  **`@EnableEnvValidation`**: Activates the validation engine.
2.  **`@ValidateEnv`**: Defines the validation rules (existence, regex patterns, defaults).

---

## 🏗️ Implementation Details

In this scenario, we use two separate configuration classes to demonstrate how `env-validator` aggregates errors from across the entire application.

### Class 1: `Scenario96Config`
- **`SCENARIO96_API_KEY`**: Mandatory existence check.
- **`SCENARIO96_EXTERNAL_URL`**: Must match `^https://.*`.

### Class 2: `Scenario96SecondaryConfig`
- **`SCENARIO96_DB_PASS`**: Mandatory existence check.
- **`SCENARIO96_EMAIL`**: Must match email regex.

---

## 🆚 `env-validator` vs. Native Spring

| Feature | Native Spring Boot | `env-validator` |
| :--- | :--- | :--- |
| **Fail-Fast** | Fails on the FIRST missing variable and stops. | Collects ALL missing variables across all classes and reports them together. |
| **Error Message** | Generic `BeanCreationException`. | Custom `MissingEnvException` with a formatted list of all violations. |
| **Regex Support** | None out-of-the-box for `@Value`. | Built-in regex validation for any variable. |

---

## 🧪 Testing the "Full Report" Behavior

1.  **Preparation**: Ensure all 4 variables (`SCENARIO96_API_KEY`, `SCENARIO96_EXTERNAL_URL`, `SCENARIO96_DB_PASS`, `SCENARIO96_EMAIL`) are missing from your environment.
2.  **Run the App**: Start the Spring Boot application.
3.  **The Result**: Instead of failing once for many times, the `env-validator` will output a single exception containing a list of **all 4 violations**.

---

## 🧪 Testing the Fail-Fast Behavior

### 1. Test Failure (Missing API Key)
Run the application without setting `SCENARIO96_API_KEY`.
The application will throw a `MissingEnvException` and terminate immediately with a clear report of all missing variables.

### 2. Test Success
Set the following environment variables:
- `SCENARIO96_API_KEY=my-secret-key`
- `SCENARIO96_EXTERNAL_URL=https://prod.api.com`

Then call the verification endpoint:
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario96/config"
```

---

## Interview Tip 💡
**Q**: *"Why validate at startup instead of just failing when the variable is first used?"*  
**A**: *"Failing at startup (Fail-Fast) is much safer for production. It prevents the application from entering a 'partial' or 'zombie' state where some features work but others fail subtly 2 hours later when a specific service is called. It ensures that if the environment isn't ready, the app doesn't launch."*

**Q**: *"What is the benefit of collecting all errors at once?"*  
**A**: *"Standard Spring `@Value` injection fails on the very first missing property. If you have 10 missing properties, you would have to restart the app 10 times to find them all. `env-validator` scans the entire context and gives you the full list in one go."*
