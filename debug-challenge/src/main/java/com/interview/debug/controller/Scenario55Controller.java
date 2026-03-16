package com.interview.debug.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario55")
public class Scenario55Controller {

    /**
     * This GET request will cause Spring Security to generate a CSRF token 
     * and send it back in a cookie named 'XSRF-TOKEN'.
     */
    @GetMapping("/token")
    public Map<String, String> getCsrfToken() {
        return Map.of(
            "message", "CSRF Token should now be present in your cookies as 'XSRF-TOKEN'.",
            "instruction", "Read the cookie and send it back in the 'X-XSRF-TOKEN' header for POST requests."
        );
    }

    /**
     * This POST request will fail if the CSRF token is missing or invalid.
     */
    @PostMapping("/update")
    public Map<String, String> protectedUpdate(@RequestBody Map<String, String> payload) {
        return Map.of(
            "status", "Success",
            "message", "CSRF validation passed! Data updated: " + payload.getOrDefault("data", "none")
        );
    }
}
