package com.interview.debug.service;

import com.interview.debug.security.JwtUtils;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Scenario 53: OAuth2 Flow Simulation
 * Demonstrates JIT (Just-in-Time) Provisioning and Role Mapping.
 */
@Service
public class OidcUserService {

    private final JwtUtils jwtUtils;
    
    // Simulating a Database for the demonstration
    private final Map<String, List<String>> userDatabase = new ConcurrentHashMap<>();

    public OidcUserService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        // Pre-seed an existing user
        userDatabase.put("tushar@google.com", List.of("ROLE_ADMIN", "SCOPE_read", "SCOPE_write"));
    }

    /**
     * The logic that bridges Google's identity with our app's roles.
     */
    public String exchangeOidcTokenForAppToken(String googleEmail, String googleName) {
        System.out.println("Processing login for: " + googleEmail);

        // 1. JIT PROVISIONING: If user doesn't exist in our DB, register them with default roles
        if (!userDatabase.containsKey(googleEmail)) {
            System.out.println("NEW USER DETECTED! Registering " + googleEmail + " with default ROLE_USER...");
            userDatabase.put(googleEmail, List.of("ROLE_USER", "SCOPE_read"));
        }

        // 2. ROLE MAPPING: Fetch roles from OUR database
        List<String> appRoles = userDatabase.get(googleEmail);

        // 3. ISSUE APP JWT: Create a new token signed by OUR app secret
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", appRoles);
        claims.put("name", googleName);
        claims.put("type", "APP_SESSION_TOKEN");

        return jwtUtils.generateToken(googleEmail, claims);
    }

    public List<String> getUserRoles(String email) {
        return userDatabase.getOrDefault(email, List.of());
    }
}
