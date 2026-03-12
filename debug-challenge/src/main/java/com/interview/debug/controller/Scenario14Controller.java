package com.interview.debug.controller;

import com.interview.debug.idempotency.Idempotent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/scenario14")
public class Scenario14Controller {

    @PostMapping("/pay")
    @Idempotent // Custom annotation triggers the Aspect
    public ResponseEntity<Map<String, Object>> processPayment(@RequestParam String amount) {
        // Simulate heavy processing or payment gateway call
        System.out.println("Processing payment of $" + amount + "...");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("transactionId", UUID.randomUUID().toString());
        response.put("amount", amount);
        response.put("message", "Payment processed successfully!");

        return ResponseEntity.ok(response);
    }
}
