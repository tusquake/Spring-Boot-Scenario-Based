package com.interview.debug.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class DefaultMessageService implements MessageService {
    @Override
    public String getWelcomeMessage() {
        return "Default Environment: No specific profile active.";
    }
}
