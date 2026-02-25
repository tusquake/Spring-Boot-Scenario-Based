package com.interview.debug.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final UserService userService;

    public NotificationService(UserService userService) {
        this.userService = userService;
    }

    public void sendNotification(String message) {
        System.out.println("Notification to " + userService.getUserName() + ": " + message);
    }
}
