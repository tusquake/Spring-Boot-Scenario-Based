# Scenario 33: Task Scheduling (@Scheduled)

## Overview
Spring Boot provides a simple yet powerful way to schedule tasks using the `@Scheduled` annotation. This scenario explores the common scheduling patterns and the subtle differences between them.

---

## 🚦 Scheduling Strategies

### 1. `fixedRate`
- **Behavior**: The task starts every N milliseconds, regardless of how long the previous execution took.
- **Use Case**: When tasks are independent and need to run at a consistent interval.
- **Risk**: If the task takes longer than the interval, multiple instances can run in parallel (if the scheduler has multiple threads).

### 2. `fixedDelay`
- **Behavior**: The task starts N milliseconds **after** the previous execution finishes.
- **Use Case**: When you want to ensure only one instance of the task runs at a time and there is always a gap between them.

### 3. `cron`
- **Behavior**: Uses Unix-like cron expressions for complex schedules (e.g., "At 10:15 AM on the last Friday of every month").

---

## 🛠️ Implementation Details
The `Scenario33Scheduler.java` class demonstrates these properties.

```java
@Scheduled(fixedRate = 5000)
public void runAtFixedRate() {
    // Starts every 5 seconds
}

@Scheduled(fixedDelay = 5000)
public void runWithFixedDelay() {
    // Starts 5 seconds AFTER last one finished
}
```

---

## 🧪 Testing the Scenario
Since these tasks run automatically, you can verify them by checking the console logs:

1. Watch the logs for `[Scenario 33]` prefixes.
2. Observe the timestamps to see the difference between `fixedRate` and `fixedDelay`.

---

## Interview Tip 💡
**Q**: *"What is the difference between fixedRate and fixedDelay?"*
**A**: *"**fixedRate** measures the time from the START of the last execution, whereas **fixedDelay** measures the time from the END of the last execution. Use `fixedDelay` if you want to prevent overlapping executions and `fixedRate` if you need a constant beat."*
