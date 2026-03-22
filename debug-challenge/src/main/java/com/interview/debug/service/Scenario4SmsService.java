package com.interview.debug.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * DEFAULT: SMS Service
 * Annotated with @Primary, so it wins if no @Qualifier is used.
 */
@Component
@Primary
public class Scenario4SmsService implements Scenario4MessageService {
    @Override
    public String sendMessage() {
        return "SMS Sent: Hello from the Primary Bean!";
    }
}
