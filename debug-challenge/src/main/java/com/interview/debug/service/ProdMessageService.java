package com.interview.debug.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class ProdMessageService implements MessageService {
    @Override
    public String getWelcomeMessage() {
        return "Production Environment: Performance and Security Optimized.";
    }
}
