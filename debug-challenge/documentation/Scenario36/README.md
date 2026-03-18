# Scenario 36: Type-Safe Configuration (@ConfigurationProperties)

Demonstrates the professional way to handle application settings compared to the scattered `@Value` approach.

## Concept
Using `@Value("${prop}")` everywhere is error-prone and messy. **Type-safe configuration** allows you to map a whole block of properties (e.g., `app.security.*`) to a single, validated Java object.

## Implementation Details
We created an `AppConfigProps` class and used constructor injection in the controller.

### Properties Snippet:
```yaml
app:
  welcome-message: "Hello!"
  max-retry-attempts: 3
  support-emails: ["a@b.com", "c@d.com"]
```

### Config Class:
```java
@ConfigurationProperties(prefix = "app")
public class AppConfigProps {
    private String welcomeMessage;
    // ... with getters/setters ...
}
```

## Verification Results
- **URL**: `/api/scenario36/config`
- **Result**: Returns a JSON object with your actual property values, proving they were correctly bound and injected.

## Interview Theory: Configuration
- **Validation**: You can add Hibernate Validation annotations (like `@Min(1)`) to your config classes to fail application startup if a property is missing or wrong.
- **Structure**: Grouping related properties into one object makes the code much easier to navigate and maintain.
- **@Value vs @ConfigurationProperties**: `@Value` is okay for a one-off secret, but `@ConfigurationProperties` is the gold standard for structured app settings.
