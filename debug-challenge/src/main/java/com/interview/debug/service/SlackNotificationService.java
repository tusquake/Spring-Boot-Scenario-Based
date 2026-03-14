package com.interview.debug.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class SlackNotificationService implements NotificationService {
    @Override
    public String send(String message) {
        return "PROD [Slack]: " + message;
    }
}
