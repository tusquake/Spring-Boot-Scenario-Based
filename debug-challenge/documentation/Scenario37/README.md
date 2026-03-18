# Scenario 37: Interface-based Service Design

Demonstrates the "Dependency Inversion" principle from SOLID.

## Concept
High-level modules (Controllers) should not depend on low-level modules (Services). Both should depend on **Abstractions (Interfaces)**. This allows you to swap implementations (e.g., switch from Email to SMS) without changing the controller.

## Implementation Details
The `Scenario37Controller` depends on a `NotificationService` interface.

### Controller Snippet:
```java
public Scenario37Controller(NotificationService notificationService) {
    this.notificationService = notificationService; // Dependency on Interface
}
```

## Verification Results
- **URL**: `/api/scenario37/notify?msg=Hello`
- **Result**: Returns a success message from whichever implementation is currently active (e.g., "Email sent: Hello").

## Interview Theory: Dependency Inversion
- **Polymorphism**: If you have two beans implementing the same interface (`EmailService` and `SmsService`), you use `@Primary` or `@Qualifier` to tell Spring which one to inject.
- **Unit Testing**: This is the secret to easy testing. You can easily inject a "Mock" notification service into the controller without needing a real SMTP server.
- **Loose Coupling**: This design makes the system more flexible. You can add a `PushNotificationService` later just by implementing the interface.
