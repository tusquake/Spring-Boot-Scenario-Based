package com.interview.debug.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scenario59")
public class Scenario59Controller {

    /**
     * Demonstrates hasAuthority("SCOPE_ADMIN").
     * This checks for the EXACT string match.
     */
    @GetMapping("/authority-test")
    public Map<String, Object> authorityTest(Authentication authentication) {
        return Map.of(
            "message", "SUCCESS: You have the SCOPE_ADMIN authority!",
            "authorities", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()),
            "explanation", "hasAuthority('SCOPE_ADMIN') matched the 'SCOPE_ADMIN' authority exactly."
        );
    }

    /**
     * Demonstrates hasRole("ADMIN").
     * This will FAIL with our current setup because it looks for 'ROLE_ADMIN'.
     */
    @GetMapping("/role-test")
    public Map<String, Object> roleTest(Authentication authentication) {
        return Map.of(
            "message", "SUCCESS: You have the ADMIN role!",
            "authorities", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()),
            "explanation", "If you see this, it means you have an authority named 'ROLE_ADMIN'."
        );
    }
}
