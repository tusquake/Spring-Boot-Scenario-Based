# Scenario 123: Thread Dump Analysis (Debugging Deadlocks)

A **Deadlock** occurs when two or more threads are blocked forever, each waiting for a resource held by another. In this scenario, we simulate a classic "circular dependency" deadlock.

## How to Trigger
Hit the following endpoint:
`GET http://localhost:8080/debug-application/scenario123/trigger`

Check the application logs. You will see:
```text
Deadlock-Thread-Alpha acquired lock1. Waiting to acquire lock2...
Deadlock-Thread-Beta acquired lock2. Waiting to acquire lock1...
Deadlock-Thread-Alpha attempting to acquire lock2...
Deadlock-Thread-Beta attempting to acquire lock1...
```
At this point, both threads are permanently stuck.

## How to Analyze

### Method 1: Spring Boot Actuator (Easiest)
If Actuator is enabled (which it is for this project), use the `/threaddump` endpoint:
`GET http://localhost:8080/debug-application/actuator/threaddump`

Search for "**Deadlock-Thread**" or scroll to the bottom of the JSON. Modern Actuator versions will explicitly list the deadlock:
```json
"deadlocks": [
    {
        "threads": [
            {
                "threadName": "Deadlock-Thread-Alpha",
                "lockInfo": { ... },
                "lockOwnerName": "Deadlock-Thread-Beta"
            },
            {
                "threadName": "Deadlock-Thread-Beta",
                "lockInfo": { ... },
                "lockOwnerName": "Deadlock-Thread-Alpha"
            }
        ]
    }
]
```

### Method 2: JVM CLI Tools
1. Find the Process ID (PID) of your Spring boot app:
   `jps -l`
2. Generate a thread dump:
   `jstack <PID>`

Search the output for "**Found one Java-level deadlock**". It will provide a stack trace for each thread, showing exactly which line of code is waiting for which lock.

### Method 3: Visual tools (JConsole / VisualVM)
Launch `jconsole`, connect to your app, and navigate to the **Threads** tab. Click the **Detect Deadlock** button at the bottom.

---

## Technical Details
The deadlock is caused by inconsistent locking order:
- **Thread Alpha**: `synchronized(lock1)` -> `synchronized(lock2)`
- **Thread Beta**: `synchronized(lock2)` -> `synchronized(lock1)`

To **fix** such deadlocks, always ensure a consistent locking order across all threads.
