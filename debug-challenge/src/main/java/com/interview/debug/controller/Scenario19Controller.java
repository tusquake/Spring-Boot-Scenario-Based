package com.interview.debug.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Scenario 19: API Version Lifecycle Management
 * Demonstrates how to handle the lifecycle of an API version:
 * Current -> Deprecated -> Sunset -> Removed
 */
@RestController
@RequestMapping("/api/scenario19")
public class Scenario19Controller {

    // --- PHASE 1: CURRENT VERSION (v2) ---
    @GetMapping("/v2/data")
    public Map<String, String> getV2Data() {
        return Map.of(
            "status", "Current",
            "message", "This is the latest supported version (V2). Full support is available.",
            "data", "Premium content for V2"
        );
    }

    // --- PHASE 2: DEPRECATED VERSION (v1) ---
    @GetMapping("/v1/deprecated")
    public ResponseEntity<Map<String, String>> getV1Deprecated() {
        return ResponseEntity.ok()
            .header("Deprecation", "Fri, 31 Dec 2025 23:59:59 GMT")
            .header("Link", "<http://localhost:8080/debug-application/api/scenario19/v2/data>; rel=\"successor-version\"")
            .body(Map.of(
                "status", "Deprecated",
                "message", "V1 is deprecated. No new features will be added. Please migrate to V2.",
                "warning", "This version will enter 'Sunset' phase in 3 months."
            ));
    }

    // --- PHASE 3: SUNSET VERSION (v1) ---
    @GetMapping("/v1/sunset")
    public ResponseEntity<Map<String, String>> getV1Sunset() {
        // Log warning for monitoring
        System.err.println("WARNING: Client accessed SUNSET endpoint /v1/sunset. Migration is urgent!");
        
        return ResponseEntity.ok()
            .header("Deprecation", "Fri, 31 Dec 2025 23:59:59 GMT")
            .header("Sunset", "Sat, 01 Mar 2026 23:59:59 GMT")
            .body(Map.of(
                "status", "Sunset",
                "message", "CRITICAL: This version is in the Sunset phase and will be removed VERY SOON.",
                "action_required", "Migrate to V2 immediately to avoid service interruption."
            ));
    }

    // --- PHASE 4: REMOVED VERSION (v1) ---
    @GetMapping("/v1/removed")
    public ResponseEntity<Map<String, String>> getV1Removed() {
        return ResponseEntity.status(HttpStatus.GONE) // 410 Gone
            .body(Map.of(
                "error", "Gone",
                "message", "API V1 has been removed permanently. You MUST use V2.",
                "migration_link", "http://localhost:8080/debug-application/api/scenario19/v2/data"
            ));
    }
}
