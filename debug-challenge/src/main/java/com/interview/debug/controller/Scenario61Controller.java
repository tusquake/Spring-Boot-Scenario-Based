package com.interview.debug.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario61")
public class Scenario61Controller {

    /**
     * Test endpoint for 401 (Authentication required).
     */
    @GetMapping("/protected")
    public Map<String, String> protectedEndpoint() {
        return Map.of("message", "You are authenticated!");
    }

    /**
     * Test endpoint for 403 (Admin role required).
     */
    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminOnlyEndpoint() {
        return Map.of("message", "Welcome, Admin!");
    }
}
