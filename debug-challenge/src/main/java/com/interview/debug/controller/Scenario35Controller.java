package com.interview.debug.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * SCENARIO 35: ENTERPRISE HTTP HEADERS
 * Demonstrates how to handle Security, Tracing, and Orchestration headers.
 */
@RestController
@RequestMapping("/api/scenario35")
public class Scenario35Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario35Controller.class);

    /**
     * 🕵️ OBSERVABILITY: Reading a Correlation ID
     * Use @RequestHeader to capture tracing IDs passed by a Gateway or Frontend.
     */
    @GetMapping("/trace")
    public ResponseEntity<Map<String, String>> traceRequest(
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        
        // If the header is missing, we usually generate one (or the Gateway does)
        String finalId = (correlationId != null) ? correlationId : UUID.randomUUID().toString();
        
        logger.info("🔍 Processing request with Correlation ID: {}", finalId);
        
        return ResponseEntity.ok()
                .header("X-Correlation-ID", finalId) // Echo it back for the client
                .body(Map.of("message", "Observability check", "traceId", finalId));
    }

    /**
     * 🚦 RATE LIMITING: Sending metadata back to the client
     * Shows how to manually set rate limit headers in a response.
     */
    @GetMapping("/limited")
    public ResponseEntity<Map<String, Object>> rateLimitDemo() {
        return ResponseEntity.ok()
                .header("X-RateLimit-Limit", "100")
                .header("X-RateLimit-Remaining", "42")
                .header("X-RateLimit-Reset", String.valueOf(System.currentTimeMillis() + 60000))
                .body(Map.of("status", "success", "info", "Check your response headers!"));
    }

    /**
     * 🤖 PROXY/GATEWAY: Reading the User's Actual IP
     * In enterprise apps, the request remote address is often the Load Balancer IP.
     * Use 'X-Forwarded-For' to get the real client.
     */
    @GetMapping("/whoami")
    public ResponseEntity<Map<String, String>> checkSource(
            @RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor,
            @RequestHeader(HttpHeaders.USER_AGENT) String userAgent) {
        
        String clientIp = (forwardedFor != null) ? forwardedFor : "Direct (No Proxy)";
        
        return ResponseEntity.ok()
                .header("X-Robots-Tag", "noindex") // Tell search engines not to index this
                .body(Map.of(
                        "clientIp", clientIp,
                        "userAgent", userAgent
                ));
    }
}
