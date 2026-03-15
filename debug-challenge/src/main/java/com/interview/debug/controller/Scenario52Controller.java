package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Scenario 52: CORS Deep Dive
 * Demonstrates how a Spring Boot application handles Cross-Origin Resource Sharing.
 */
@RestController
@RequestMapping("/api/scenario52")
public class Scenario52Controller {

    @GetMapping("/data")
    public Map<String, String> getSecureData() {
        return Map.of(
            "message", "This is sensitive data accessible only via CORS-approved origins.",
            "status", "Success",
            "cors_configured", "true"
        );
    }

    @GetMapping("/public")
    public Map<String, String> getPublicData() {
        return Map.of("message", "This is public data.");
    }
}
