package com.interview.debug.controller;

import com.interview.debug.event.UserRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario28")
public class Scenario28Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario28Controller.class);
    private final ApplicationEventPublisher eventPublisher;

    public Scenario28Controller(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/register")
    public Map<String, Object> registerUser(@RequestParam String username) {
        logger.info("[Main-Thread] Registering user: {}", username);
        
        // 1. Core Logic: Save to DB (Skipped for simplicity, just log it)
        logger.info("[Main-Thread] User {} saved to database.", username);
        
        // 2. Publish Event: Decouple side-effects
        UserRegistrationEvent event = new UserRegistrationEvent(username);
        eventPublisher.publishEvent(event);
        
        logger.info("[Main-Thread] Event published. Returning response to client.");
        
        return Map.of(
            "status", "success",
            "message", "User " + username + " registered. Notifications are being sent asynchronously.",
            "checkLogs", "Look for [Async-Email] and [Async-Slack] tags in the console."
        );
    }
}
