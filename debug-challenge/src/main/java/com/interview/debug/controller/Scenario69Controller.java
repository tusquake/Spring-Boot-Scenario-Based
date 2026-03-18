package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario69")
public class Scenario69Controller {
    /**
     * This endpoint is protected by RateLimitingFilter.
     * Use this to demonstrate DDoS protection (per-IP throttling).
     */
    @GetMapping("/secure-data")
    public String getSecureData() {
        return "SUCCESS: You have access to this sensitive data. Try refreshing quickly!";
    }
}
