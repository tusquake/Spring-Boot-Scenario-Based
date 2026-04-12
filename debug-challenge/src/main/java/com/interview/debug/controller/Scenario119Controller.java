package com.interview.debug.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario119")
public class Scenario119Controller {

    @GetMapping("/header-test")
    public ResponseEntity<String> testHeader(@RequestHeader("X-Custom-Header") String customHeader) {
        return ResponseEntity.ok("Header received: " + customHeader);
    }

    @GetMapping("/param-test")
    public ResponseEntity<String> testParam(@RequestParam(name = "name", defaultValue = "Guest") String name) {
        return ResponseEntity.ok("Hello, " + name);
    }

    @GetMapping("/exception-test")
    public ResponseEntity<String> testException(@RequestParam("trigger") boolean trigger) {
        if (trigger) {
            throw new IllegalArgumentException("Triggered custom exception");
        }
        return ResponseEntity.ok("No exception");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @PostMapping("/complex-json")
    public ResponseEntity<Map<String, Object>> complexJson(@RequestBody Map<String, Object> payload) {
        payload.put("processed", true);
        payload.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }
}
