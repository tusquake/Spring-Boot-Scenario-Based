# Scenario 18: Custom Spring Boot Starters

Demonstrates the "Magic" of Spring Boot: Auto-configuration and custom starters.

## Concept
A **Starter** is a library that automatically sets itself up when it finds certain properties or classes on the classpath. This is how `spring-boot-starter-data-jpa` works!

## Implementation Details
We implemented a `CustomBannerService` that only activates if a certain property is set.

### Auto-Config Snippet:
```java
@Configuration
@ConditionalOnProperty(name = "custom.starter.enabled", havingValue = "true")
public class CustomStarterAutoConfiguration {
    @Bean
    public CustomBannerService bannerService() { ... }
}
```

## Verification Results
1. **Disabled**: If `custom.starter.enabled=false`, the `/api/scenario18/check-starter` returns "Custom Starter is DISABLED".
2. **Enabled**: Change property to `true` and restart. Now it returns "ACTIVE!" and prints a banner in the console.

## Interview Theory: Under the Hood
- **spring.factories**: How does Spring know where to look for your configuration? You must list it in `src/main/resources/META-INF/spring.factories` (or `org.springframework.boot.autoconfigure.AutoConfiguration.imports` in newer versions).
- **Conditional Annotations**: Be ready to name others like `@ConditionalOnClass` (activates if a library is present) or `@ConditionalOnMissingBean` (allows users to override your default beans).
- **The Philosophy**: "Opinionated but overridable." Provide good defaults but stay out of the way if the user wants to take over.
