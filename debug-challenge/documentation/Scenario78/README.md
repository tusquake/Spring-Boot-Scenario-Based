# Scenario 78: Spring Boot Profiles & Configuration

## Overview
**Spring Profiles** provide a way to segregate parts of your application configuration and make it only available in certain environments. This is crucial for managing different databases, API keys, and log levels between Local, Dev, and Production.

## Key Concepts 🌍

### 1. Profile-Specific Properties
By naming your properties files `application-{profile}.properties`, Spring Boot will automatically load them when that profile is active.
- `application-dev.properties` -> Loaded if `dev` profile is active.
- `application-prod.properties` -> Loaded if `prod` profile is active.

### 2. @Profile Annotation
You can use `@Profile` on beans to ensure they are only instantiated in specific environments.
```java
@Service
@Profile("dev")
public class DevMessageService implements MessageService { ... }
```

### 3. Property Precedence
Profile-specific properties always **override** those in the default `application.properties`. 
For example, if `app.env` is defined in both, the version in `application-dev.properties` wins if `dev` is active.

## How to Set the Active Profile 🚀
There are multiple ways to trigger a profile:
1. **JVM System Property**: `-Dspring.profiles.active=dev`
2. **Environment Variable**: `export SPRING_PROFILES_ACTIVE=prod`
3. **In application.properties**: `spring.profiles.active=dev` (Not recommended for prod).

## Interview Tips 💡
- **What happens if no profile is active?** The `default` profile is active.
- **Can multiple profiles be active?** Yes, separated by commas: `dev,h2,local`.
- **How to exclude a profile?** Use `!`: `@Profile("!prod")`.

## Testing the Scenario
1. **Check Current Profile**: `GET /api/scenario78/status`
   - By default, it will show `active_profiles: []` and the **Default** message service.
2. **Switching Profiles**: To test the switch, you would normally restart the app with `-Dspring.profiles.active=dev`.
