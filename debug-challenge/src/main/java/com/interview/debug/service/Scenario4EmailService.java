package com.interview.debug.service;

import org.springframework.stereotype.Component;

/**
 * SPECIFIC: Email Service
 * We give it a specific name 'emailService' so we can pick it with @Qualifier.
 */
@Component("emailService")
public class Scenario4EmailService implements Scenario4MessageService {
    @Override
    public String sendMessage() {
        return "Email Sent: Hello from the Qualified Bean!";
    }
}
