# Scenario 28: Event-Driven Design (Spring Events)

Demonstrates how to decouple components by using an Event Bus instead of direct service calls.

## Concept
In a typical app, registration involves: Saving to DB -> Sending Email -> Sending Slack Notification. If you call all these in one service, the service becomes bloated and slow. Using **Events**, the Registration service just "announces" the user was created, and other listeners handle the side effects.

## Implementation Details
Using `ApplicationEventPublisher` and `@EventListener` (plus `@Async` for performance).

### Publisher:
```java
UserRegistrationEvent event = new UserRegistrationEvent(username);
eventPublisher.publishEvent(event);
```

### Async Listener:
```java
@Async
@EventListener
public void handleRegistration(UserRegistrationEvent event) {
    logger.info("[Async-Email] Sending welcome email to {}", event.getUsername());
}
```

## Verification Results
- **URL**: `/api/scenario28/register?username=Alice`
- **Check Logs**:
  1. `[Main-Thread] Registering user...`
  2. `[Main-Thread] Event published. Returning response...` (Fast response!)
  3. `[Async-Email] Sending welcome email...` (Happens a few ms later on a different thread).

## Interview Theory: Events
- **Synchronous vs Asynchronous**: By default, Spring events are synchronous. You must add `@EnableAsync` and `@Async` to make them non-blocking.
- **Decoupling**: The Registration service doesn't need to know that an Email service or Slack service exists.
- **Transactionality**: Use `@TransactionalEventListener` if you only want the event to trigger AFTER the database transaction has successfully committed.
