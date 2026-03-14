package com.interview.debug.controller;

import com.interview.debug.config.AppConfigProps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * SCENARIO 36: TYPE-SAFE CONFIGURATION DEMO
 */
@RestController
@RequestMapping("/api/scenario36")
public class Scenario36Controller {

    private final AppConfigProps config;

    // Injection via Constructor is the best practice
    public Scenario36Controller(AppConfigProps config) {
        this.config = config;
    }

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        return Map.of(
                "welcome", config.getWelcomeMessage(),
                "retries", config.getMaxRetryAttempts(),
                "emails", config.getSupportEmails(),
                "features", config.getFeatureFlags()
        );
    }
}
