# Spring Application Events — Complete Interview Reference

## Table of Contents
1. [What are Application Events?](#1-what-are-events)
2. [Why Use Events? (Decoupling)](#2-why-events)
3. [Publishing an Event (ApplicationEventPublisher)](#3-publishing)
4. [Listening to an Event (@EventListener)](#4-listening)
5. [The Classic Interview Trap: Synchronous vs Asynchronous Events](#5-the-classic-interview-trap-sync-vs-async)
6. [Transactional Events (@TransactionalEventListener)](#6-transactional-events)
7. [Ordering Event Listeners (@Order)](#7-ordering)
8. [Conditional Event Listeners (@EventListener(condition = "..."))](#8-conditional)
9. [Generic Events and Type Safety](#9-generic-events)
10. [Standard Spring Boot Events (Startup, Shutdown)](#10-standard-events)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What are Application Events?
Spring Events allow you to implement the **Observer Pattern** within a single Spring application. One component publishes an event, and one or more other components listen for it and react.

---

## 2. Why Use Events? (Decoupling)
Events allow you to separate your core business logic from side-effects. For example, a `UserRegistrationService` should only care about saving the user to the database. Sending a welcome email or updating a search index should be handled by listeners, keeping the core service clean and easy to test.

---

## 3. Publishing an Event
Use the `ApplicationEventPublisher` to broadcast your event. Since Spring 4.2, events no longer need to extend `ApplicationEvent`.
```java
eventPublisher.publishEvent(new MyCustomEvent("Data"));
```

---

## 4. Listening to an Event
Annotate a method with `@EventListener`.
```java
@EventListener
public void handleMyEvent(MyCustomEvent event) {
    System.out.println("Received: " + event.getData());
}
```

---

## 5. The Classic Interview Trap: Synchronous vs Asynchronous
**The Trap**: By default, Spring Events are **SYNCHRONOUS**. The publisher thread waits for all listeners to finish before continuing. If a listener is slow, the whole request is slow.
**The Fix**: Use `@Async` on the listener method to run it in a background thread.

---

## 6. Transactional Events
Use `@TransactionalEventListener` to ensure that a listener only runs after the publisher's transaction has successfully committed. This prevents sending a welcome email if the database transaction rolled back.

---

## 7. Ordering Event Listeners
If you have multiple listeners for the same event, use `@Order` to control which one runs first.

---

## 8. Conditional Event Listeners
You can use SpEL (Spring Expression Language) to filter events:
`@EventListener(condition = "#event.success == true")`.

---

## 9. Generic Events and Type Safety
Spring supports generic events (e.g., `MyEvent<User>`). You can listen for specific types without manually casting in your code.

---

## 10. Standard Spring Boot Events
Spring Boot emits several events you can hook into:
- `ApplicationReadyEvent`: When the app is fully started and ready for traffic.
- `ApplicationFailedEvent`: If the app fails to start.

---

## 11. Common Mistakes
1. Assuming events are asynchronous by default.
2. Publishing an event before the transaction is committed (leading to data inconsistency).
3. Creating too many events, making the application flow hard to follow ("Spaghetti Events").

---

## 12. Quick-Fire Interview Q&A
**Q: Can one event have multiple listeners?**  
A: Yes, all matching listeners will be executed.  
**Q: What is the difference between @EventListener and @TransactionalEventListener?**  
A: `@EventListener` runs immediately; `@TransactionalEventListener` can be synchronized with transaction phases (e.g., AFTER_COMMIT).
