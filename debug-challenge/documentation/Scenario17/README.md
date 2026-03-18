# Scenario 17: Logback & Logging Levels

Demonstrates structured logging and how to configure different depths of detail for troubleshooting.

## Concept
1. **Levels**: `ERROR > WARN > INFO > DEBUG > TRACE`.
2. **Dynamic Configuration**: You can change levels in production without restarting (if using Spring Boot Actuator).
3. **Rotation**: Automatically splitting logs into daily files (`app.2023-10-27.log`) to prevent a single file from consuming all disk space.

## Implementation Details
The controller triggers logs at different levels and simulates an exception to show error logging.

### Logback Config Example:
```xml
<root level="INFO">
    <appender-ref ref="ROLLING" />
</root>
```

## Verification Results
- **URL**: `http://localhost:8080/api/scenario17/generate-logs`
- **Output**: Check the `logs/` folder. You will see `app.log` containing the info, warn, and error messages.
- **Note**: The DEBUG log only appears if you set `logging.level.com.interview=DEBUG` in `application.properties`.

## Interview Theory: Logging Best Practices
- **SLF4J**: Always use the SLF4J interface rather than a direct implementation like Log4j2.
- **The "Cost" of Debug**: Never use string concatenation in logs: `logger.debug("Data: " + data)`. Use placeholders instead: `logger.debug("Data: {}", data)`. This prevents the string concatenation from happening if the DEBUG level is turned off.
- **ELK Stack**: In microservices, logs are usually pushed to a central server like **Elasticsearch** (via Logstash) and viewed in **Kibana**.
