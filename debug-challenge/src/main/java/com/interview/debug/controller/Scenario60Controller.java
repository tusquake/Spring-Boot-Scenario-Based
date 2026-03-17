package com.interview.debug.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario60")
public class Scenario60Controller {

    /**
     * This endpoint strictly requires ROLE_USER.
     * With Role Hierarchy (ADMIN > USER), an ADMIN can also access this!
     */
    @GetMapping("/user-only")
    @PreAuthorize("hasRole('USER')")
    public Map<String, String> userOnlyEndpoint() {
        return Map.of(
            "message", "Access Granted!",
            "detail", "If you are an ADMIN, you got in because of Role Hierarchy (ADMIN > USER)."
        );
    }
}
