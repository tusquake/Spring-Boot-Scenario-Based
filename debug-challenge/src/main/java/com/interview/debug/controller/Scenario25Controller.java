package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario25")
public class Scenario25Controller {

    /**
     * 1. URL Versioning
     * Access via: /api/scenario25/v1/items
     */
    @GetMapping("/v1/items")
    public Map<String, Object> getItemsV1() {
        return Map.of(
            "version", "v1",
            "strategy", "URL Path",
            "data", Map.of("id", 1, "name", "Legacy Item")
        );
    }

    /**
     * 2. Custom Header Versioning
     * Access via: /api/scenario25/items with header X-API-VERSION=2
     */
    @GetMapping(value = "/items", headers = "X-API-VERSION=2")
    public Map<String, Object> getItemsV2() {
        return Map.of(
            "version", "v2",
            "strategy", "Custom Header (X-API-VERSION)",
            "data", Map.of("id", 1, "fullName", "Modern Item v2")
        );
    }

    /**
     * 3. Media Type (Content Negotiation) Versioning
     * Access via: /api/scenario25/items with header Accept=application/vnd.company.v3+json
     */
    @GetMapping(value = "/items", produces = "application/vnd.company.v3+json")
    public Map<String, Object> getItemsV3() {
        return Map.of(
            "version", "v3",
            "strategy", "Media Type (Accept Header)",
            "data", Map.of("id", 1, "fullName", "State of the Art Item v3", "tags", new String[]{"new", "cool"})
        );
    }
}
