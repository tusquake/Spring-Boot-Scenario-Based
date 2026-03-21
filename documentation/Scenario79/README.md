# Scenario 79: Advanced Logging in Spring Boot

## Overview
Logging is one of the most important aspects of production support. Spring Boot uses **Logback** as its default logging provider but supports others like Log4j2. Configuring it correctly ensures that your logs are readable, searchable, and don't fill up the disk.

## Key Concepts 🪵

### 1. Log Levels
Spring Boot supports standard log levels: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`, `OFF`.
- **Default**: `INFO` is the default level for Spring.
- **Config**: `logging.level.<package>=<level>` in `application.properties`.

### 2. Logback Configuration (`logback-spring.xml`)
By placing a `logback-spring.xml` file in your resources, you take full control of logging.
- **Appenders**: Define where the logs go (Console, File, Network).
- **Rolling Policy**: Crucial for production. It ensures logs are rotated (e.g., a new file every day or every 10MB) to prevent disk exhaustion.
- **Spring Profiles**: You can use `<springProfile>` to have different logging for local vs. production.

### 3. SLF4J (Simple Logging Facade for Java)
Always use the SLF4J abstraction instead of a concrete logger. This allows you to switch logging providers without changing your code.
```java
@Slf4j // Lombok annotation
public class MyService {
    public void doWork() {
        log.info("Processing work for id: {}", id);
    }
}
```

## Interview Tips 💡
- **Why use SLF4J?** It's a facade. It decouples your code from the implementation (Logback, Log4j2).
- **What is Structured Logging?** Logging in JSON format so that tools like ELK (Elasticsearch, Logstash, Kibana) or Splunk can parse them easily.
- **PII Leakage**: Never log sensitive data like passwords, credit card numbers, or auth tokens. Use masking if necessary.

## Testing the Scenario
1. **Trigger Logs**: `GET /api/scenario79/log?message=HelloWorld`
2. **Check Output**: 
   - View your IDE console for color-coded logs.
   - Check the file `logs/app.log` in the project root.
3. **Verify Settings**: Observe how `DEBUG` logs appear for `com.interview.debug` but not for other libraries because of our `application.properties` settings.
