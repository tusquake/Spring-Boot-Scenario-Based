package com.interview.debug.controller;

import com.interview.debug.config.SecuritySettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario76")
public class Scenario76Controller {

    // 1. Using @Value (Simple, but no validation/relaxed-binding support in the same way)
    @Value("${app.security.token-timeout}")
    private int tokenTimeoutValue;

    // 2. Using @ConfigurationProperties bean (Type-safe, Grouped, Validated)
    private final SecuritySettings securitySettings;

    public Scenario76Controller(SecuritySettings securitySettings) {
        this.securitySettings = securitySettings;
    }

    @GetMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareConfig() {
        Map<String, Object> response = new HashMap<>();
        
        // Data from @Value
        response.put("value_tokenTimeout", tokenTimeoutValue);
        
        // Data from @ConfigurationProperties
        response.put("configProps_tokenTimeout", securitySettings.getTokenTimeout());
        response.put("configProps_maxRetries", securitySettings.getMaxRetries());
        response.put("configProps_encryption_algo", securitySettings.getEncryption().getAlgorithm());
        response.put("configProps_encryption_keySize", securitySettings.getEncryption().getKeySize());
        
        return ResponseEntity.ok(response);
    }
}
