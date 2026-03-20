package com.interview.debug.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevMessageService implements MessageService {
    @Override
    public String getWelcomeMessage() {
        return "Dev Environment: Comprehensive Logging Enabled.";
    }
}
