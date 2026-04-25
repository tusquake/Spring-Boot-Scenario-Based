# Log Levels & SLF4J — Complete Interview Reference

## Table of Contents
1. [Introduction to Logging Frameworks](#1-introduction)
2. [SLF4J vs Logback vs Log4j2](#2-frameworks-comparison)
3. [Understanding Log Levels (TRACE to ERROR)](#3-log-levels)
4. [Using Lombok's @Slf4j](#4-lombok-slf4j)
5. [The Classic Interview Trap: Parameterized Logging](#5-the-classic-interview-trap-params)
6. [Configuring Log Levels in application.properties](#6-configuration)
7. [Externalizing Logback Config (logback.xml)](#7-logback-xml)
8. [Log Appenders (Console, File, Rolling)](#8-appenders)
9. [Logging in Production: Best Practices](#9-production-logging)
10. [MDC (Mapped Diagnostic Context) Recap](#10-mdc)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Logging is the eyes and ears of a production application. It allows developers to understand the internal state of the system, debug issues, and monitor performance.

---

## 2. SLF4J vs Logback
- **SLF4J**: A simple facade (interface) for logging. It doesn't do the logging itself but provides a consistent API.
- **Logback**: The default implementation in Spring Boot. It's the successor to Log4j and is faster and more flexible.

---

## 3. Understanding Log Levels
1. **TRACE**: Very detailed (e.g., individual SQL parameters).
2. **DEBUG**: Useful information for debugging.
3. **INFO**: (Default) General progress of the application.
4. **WARN**: Potential issues that don't stop the app (e.g., "Retrying DB call").
5. **ERROR**: Serious issues (e.g., "Exception caught, request failed").

---

## 4. Using Lombok @Slf4j
Lombok makes logging cleaner by automatically injecting a `log` field into your class:
```java
@Slf4j
public class MyService {
    public void doWork() { log.info("Working..."); }
}
```

---

## 5. The Classic Interview Trap: String Concatenation
**The Trap**: You use string concatenation in a log line:
`log.debug("The user is " + user.getName());`
**The Problem**: Even if the log level is set to INFO (meaning debug logs are hidden), the string concatenation still happens, wasting CPU and memory.
**The Fix**: Use placeholders:
`log.debug("The user is {}", user.getName());`
SLF4J will only perform the string formatting if the DEBUG level is actually enabled.

---

## 6. Configuration
You can set log levels for specific packages in `application.properties`:
`logging.level.com.interview.debug=DEBUG`
`logging.level.org.springframework.web=INFO`

---

## 7. logback.xml
For advanced configuration (like daily rolling log files, coloring, or sending logs to a remote server), you should use a `logback-spring.xml` file in your resources folder.

---

## 8. Log Appenders
- **ConsoleAppender**: Writes logs to the terminal.
- **FileAppender**: Writes logs to a file.
- **RollingFileAppender**: Creates a new file every day (or every 100MB) to prevent disk space exhaustion.

---

## 9. Production Best Practices
- Never log sensitive data (Passwords, Credit Cards, PII).
- Use `ERROR` only for things that require immediate attention from a human.
- Use `JSON` format for logs if you are using a log aggregator like **ELK Stack** or **Splunk**.

---

## 10. MDC (Mapped Diagnostic Context)
Used to add contextual information (like a `traceId`) to every log line without passing it through your methods.

---

## 11. Common Mistakes
1. Logging everything at the `INFO` level.
2. Leaving `TRACE` logging on in production (kills performance).
3. Not including a timestamp and thread name in the log pattern.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I change the log level without restarting?**  
A: Yes, if you use Spring Boot Actuator's `/loggers` endpoint.  
**Q: What is the benefit of SLF4J?**  
A: It decouples your code from the underlying logging library, making it easy to switch from Logback to Log4j2 if needed.
