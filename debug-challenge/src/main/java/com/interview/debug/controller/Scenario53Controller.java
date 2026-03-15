package com.interview.debug.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Scenario 53: OAuth2 Resource Server
 * Demonstrates Scope-based Authorization using JWT Scopes.
 */
@RestController
@RequestMapping("/api/scenario53")
public class Scenario53Controller {

    @GetMapping("/public")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "This is a public endpoint, no token needed.");
    }

    @GetMapping("/read")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public Map<String, String> readData() {
        return Map.of(
            "message", "Successfully accessed READ data!",
            "scope_required", "SCOPE_read",
            "status", "Authorized"
        );
    }

    @GetMapping("/write")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    public Map<String, String> writeData() {
        return Map.of(
            "message", "Successfully accessed WRITE data!",
            "scope_required", "SCOPE_write",
            "status", "Authorized"
        );
    }
}
