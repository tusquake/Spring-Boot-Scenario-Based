# Scenario 40: Feature Toggles (@ConditionalOnProperty)

Demonstrates how to enable or disable entire features at runtime using configuration properties.

## Concept
Feature Toggles (or Flags) allow you to ship code to production but keep it hidden until you're ready. This is the foundation of **Continuous Deployment** and **A/B Testing**.

## Implementation Details
We used `@ConditionalOnProperty` to control whether the `BetaFeatureService` bean is even created by Spring.

### Service Snippet:
```java
@Service
@ConditionalOnProperty(name = "feature.beta.enabled", havingValue = "true")
public class BetaFeatureService { ... }
```

### Controller Snippet:
```java
// We use Optional because the bean might be missing!
private final Optional<BetaFeatureService> betaService;
```

## Verification Results
1. **Disabled**: Set `feature.beta.enabled=false` (or omit it) in `application.properties`.
   - Result: `/api/scenario40/test` returns "Beta feature is CURRENTLY DISABLED".
2. **Enabled**: Set `feature.beta.enabled=true` and restart.
   - Result: Returns "Beta logic executed successfully!".

## Interview Theory: Feature Management
- **Conditional Beans**: Mention that Spring provides many others like `@ConditionalOnClass` (load if a library is present) and `@ConditionalOnMissingBean` (allow user to override).
- **The "Optional" Pattern**: Always use `Optional` when injecting conditional beans to avoid `NullPointerException`.
- **Cloud Config**: In a real app, these properties are often managed in **Spring Cloud Config** or **LaunchDarkly**, allowing you to toggle features without a restart.
