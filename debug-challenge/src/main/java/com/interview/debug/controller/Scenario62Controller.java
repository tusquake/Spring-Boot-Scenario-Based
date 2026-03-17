package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario62")
public class Scenario62Controller {

    /**
     * This endpoint is handled via .permitAll() in HttpSecurity.
     * It STAYS in the filter chain, so it will have security headers.
     */
    @GetMapping("/permitted")
    public Map<String, String> permittedEndpoint() {
        return Map.of(
            "type", "PermitAll",
            "observation", "Check your response headers. You should see CSP and other security headers."
        );
    }

    /**
     * This endpoint is handled via web.ignoring().
     * It BYPASSES the filter chain, so it will NOT have security headers.
     */
    @GetMapping("/ignored")
    public Map<String, String> ignoredEndpoint() {
        return Map.of(
            "type", "WebSecurity Ignoring",
            "observation", "Check your response headers. You will NOT see security headers (No CSP, No XSS Protection, etc.)."
        );
    }
}
