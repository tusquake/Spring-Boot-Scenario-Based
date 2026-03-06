package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugErrorController {

    @GetMapping("/api/test-error")
    public String triggerError(@RequestParam String type) {
        if ("400".equals(type)) {
            // This will be caught by IllegalArgumentException handler
            throw new IllegalArgumentException("Invalid input data provided!");
        } else if ("404".equals(type)) {
            // Simulating a not found error
            throw new NullPointerException(); 
        } else if ("500".equals(type)) {
            throw new RuntimeException("Something went wrong internally!");
        }
        return "Everything is fine!";
    }
}
