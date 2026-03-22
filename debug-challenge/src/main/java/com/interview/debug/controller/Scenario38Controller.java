package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Scenario 38: API Versioning Strategies
 * Demonstrates the 3 most common ways to version REST APIs in Spring.
 */
@RestController
@RequestMapping("/api/scenario38")
public class Scenario38Controller {

    // --- STRATEGY 1: URI Versioning (Most Common) ---
    // Simple, easy to bookmark, but changes the URL.
    @GetMapping("/v1/test")
    public Map<String, String> getV1() {
        return Map.of("version", "V1", "strategy", "URI Path-based (/v1/...)");
    }

    @GetMapping("/v2/test")
    public Map<String, String> getV2() {
        return Map.of("version", "V2", "strategy", "URI Path-based (/v2/...)");
    }

    // --- STRATEGY 2: Header Versioning ---
    // Keeps URL clean. Uses the 'headers' attribute of @GetMapping.
    @GetMapping(value = "/header/test", headers = "X-API-VERSION=1")
    public Map<String, String> getHeaderV1() {
        return Map.of("version", "V1", "strategy", "Custom Header (X-API-VERSION=1)");
    }

    @GetMapping(value = "/header/test", headers = "X-API-VERSION=2")
    public Map<String, String> getHeaderV2() {
        return Map.of("version", "V2", "strategy", "Custom Header (X-API-VERSION=2)");
    }

    // --- STRATEGY 3: Media Type Versioning (Accept Header) ---
    // Also known as "Content Negotiation". Very "Restful".
    @GetMapping(value = "/media/test", produces = "application/vnd.company.v1+json")
    public Map<String, String> getMediaV1() {
        return Map.of("version", "V1", "strategy", "Media Type (Accept Header)");
    }

    @GetMapping(value = "/media/test", produces = "application/vnd.company.v2+json")
    public Map<String, String> getMediaV2() {
        return Map.of("version", "V2", "strategy", "Media Type (Accept Header)");
    }
}
