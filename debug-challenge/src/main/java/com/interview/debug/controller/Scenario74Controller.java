package com.interview.debug.controller;

import com.interview.debug.util.SsrfValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/api/scenario74")
public class Scenario74Controller {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * VULNERABLE ENDPOINT
     * Takes a URL and fetches it without validation.
     */
    @GetMapping("/vulnerable/fetch")
    public ResponseEntity<String> vulnerableFetch(@RequestParam String url) {
        try {
            // DANGEROUS: Directly fetching user-provided URL
            String response = restTemplate.getForObject(new URI(url), String.class);
            return ResponseEntity.ok("Vulnerable Fetch Success: " + 
                (response != null ? response.substring(0, Math.min(response.length(), 200)) : "empty"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * SECURE ENDPOINT
     * Validates the URL before fetching.
     */
    @GetMapping("/secure/fetch")
    public ResponseEntity<String> secureFetch(@RequestParam String url) {
        if (!SsrfValidator.isValidUrl(url)) {
            return ResponseEntity.badRequest().body("Blocked: Invalid or prohibited URL (SSRF Protection)");
        }

        try {
            String response = restTemplate.getForObject(new URI(url), String.class);
            return ResponseEntity.ok("Secure Fetch Success: " + 
                (response != null ? response.substring(0, Math.min(response.length(), 200)) : "empty"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
