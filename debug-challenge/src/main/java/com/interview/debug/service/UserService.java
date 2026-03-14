package com.interview.debug.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final NotificationService notificationService;

    public UserService(@Lazy NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public String getUserName() {
        return "John Doe";
    }

    public void processUser() {
        notificationService.send("Processing user...");
    }
}
