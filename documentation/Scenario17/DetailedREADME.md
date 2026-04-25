# Advanced Logging in Spring Boot — Complete Interview Reference

## Table of Contents
1. [Logging Frameworks in Spring (SLF4J vs Logback)](#1-logging-frameworks)
2. [Logging Levels (DEBUG, INFO, WARN, ERROR)](#2-logging-levels)
3. [Spring Profiles in Logback](#3-spring-profiles-in-logback)
4. [Appenders (Console vs File)](#4-appenders)
5. [The Classic Interview Trap: Performance & String Concatenation](#5-the-classic-interview-trap-performance)
6. [Rolling Policies (Size vs Time Based)](#6-rolling-policies)
7. [Log Archival and Retention](#7-archival)
8. [JSON Logging for ELK/CloudWatch](#8-json-logging)
9. [Contextual Logging (MDC - Mapped Diagnostic Context)](#9-mdc)
10. [Changing Log Levels Dynamically](#10-dynamic-log-levels)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Logging Frameworks in Spring (SLF4J vs Logback)
- **SLF4J (Simple Logging Facade for Java)**: A facade/API. You write code against SLF4J, so you can swap the underlying implementation later without changing your code.
- **Logback**: The default implementation used by Spring Boot. It is faster and has a smaller footprint than its predecessor, Log4j.

---

## 2. Logging Levels (DEBUG, INFO, WARN, ERROR)
- **TRACE**: Finest details (very noisy).
- **DEBUG**: Information useful for debugging.
- **INFO**: Standard business events (startup, user login, etc.).
- **WARN**: Potential issues that don't stop the app (e.g., fallback used).
- **ERROR**: Serious failures (exceptions, database down).

---

## 3. Spring Profiles in Logback
You can use `<springProfile>` tags in `logback-spring.xml` to define different logging behaviors for different environments (e.g., `DEBUG` in Dev, `INFO` in Prod).

---

## 4. Appenders (Console vs File)
An **Appender** is responsible for writing log events. 
- `ConsoleAppender`: Writes to `System.out`.
- `RollingFileAppender`: Writes to a file and handles rotation.

---

## 5. The Classic Interview Trap: Performance & String Concatenation
**The Trap**: Doing this: `logger.debug("User " + user.getName() + " logged in.");`
**Why?**: Even if the log level is `INFO`, the JVM still performs the string concatenation before calling the logger.
**The Fix**: Use placeholders: `logger.debug("User {} logged in.", user.getName());`. The string is only built if the `DEBUG` level is actually active.

---

## 6. Rolling Policies (Size vs Time Based)
Rolling policies prevent a single log file from growing until it fills the disk.
- **TimeBased**: New file every day/hour.
- **SizeBased**: New file every 10MB.
- **SizeAndTimeBased**: Combines both (Recommended).

---

## 7. Log Archival and Retention
Use `maxHistory` and `totalSizeCap` to ensure old logs are deleted automatically after a certain number of days or when they reach a total size limit.

---

## 8. JSON Logging for ELK/CloudWatch
For modern cloud environments, logs should be written in JSON format. This makes them easy to parse by log aggregators like Logstash or Splunk.

---

## 9. Contextual Logging (MDC)
MDC (Mapped Diagnostic Context) allows you to add information (like a `userId` or `requestId`) to every log line in a thread without manually passing it to every logger call.

---

## 10. Changing Log Levels Dynamically
You can change log levels at runtime without restarting the app using **Spring Boot Actuator** (`POST /actuator/loggers/<name>`).

---

## 11. Common Mistakes
1. Committing log files to Git.
2. Logging sensitive information (passwords, PII).
3. Using `e.printStackTrace()` or `System.out` instead of a logger.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the difference between logback.xml and logback-spring.xml?**  
A: `logback-spring.xml` is processed by Spring, allowing you to use Spring-specific features like `<springProfile>`.  
**Q: How do you log a full stack trace?**  
A: `logger.error("Error occurred: ", e);` (The exception is the last argument).
