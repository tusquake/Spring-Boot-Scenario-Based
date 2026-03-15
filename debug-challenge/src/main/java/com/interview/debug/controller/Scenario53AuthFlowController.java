package com.interview.debug.controller;

import com.interview.debug.service.OidcUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Scenario 53: OAuth2 Flow Simulation
 * Endpoint to simulate the exchange of a social identity for an app JWT.
 */
@RestController
@RequestMapping("/api/scenario53/auth")
public class Scenario53AuthFlowController {

    private final OidcUserService oidcUserService;

    public Scenario53AuthFlowController(OidcUserService oidcUserService) {
        this.oidcUserService = oidcUserService;
    }

    /**
     * Simulation of the OAuth2 Callback/Exchange.
     * In a real app, this would be handled by Spring Security OAuth2 Client, 
     * but we are showing the internal logic here.
     */
    @PostMapping("/exchange")
    public Map<String, String> simulateExchange(
            @RequestParam String googleEmail, 
            @RequestParam String googleName) {
        
        // The core flow:
        // 1. We "Trust" the email because Google verified it (SIMULATED)
        // 2. We map it to our internal DB roles
        // 3. We issue our OWN tokens
        String appToken = oidcUserService.exchangeOidcTokenForAppToken(googleEmail, googleName);

        return Map.of(
            "status", "Successfully Exchanged",
            "provider", "Google",
            "app_token", appToken,
            "message", "Use this token as 'Bearer " + appToken + "' for subsequent calls."
        );
    }
}
