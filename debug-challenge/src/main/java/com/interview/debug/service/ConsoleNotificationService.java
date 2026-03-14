package com.interview.debug.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default") // Active when no other profile is chosen
public class ConsoleNotificationService implements NotificationService {
    @Override
    public String send(String message) {
        return "DEV [Console]: " + message;
    }
}
