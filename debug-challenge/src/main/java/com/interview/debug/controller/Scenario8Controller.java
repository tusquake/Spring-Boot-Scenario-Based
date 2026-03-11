package com.interview.debug.controller;

import com.interview.debug.security.JwtUtils;
import com.interview.debug.service.TokenBlacklistService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenario8")
public class Scenario8Controller {

    private final JwtUtils jwtUtils;
    private final TokenBlacklistService blacklistService;

    public Scenario8Controller(JwtUtils jwtUtils, TokenBlacklistService blacklistService) {
        this.jwtUtils = jwtUtils;
        this.blacklistService = blacklistService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username) {
        // In real app, validate password first
        String token = jwtUtils.generateToken(username);
        return "Login Success! Token: " + token;
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String jti = jwtUtils.extractJti(token);
            
            // BLACKLISTING: The core of the fix!
            blacklistService.blacklistToken(jti);
            return "Logout Success! Token blacklisted.";
        }
        return "Invalid logout request.";
    }

    @GetMapping("/protected")
    public String protectedResource() {
        return "Congrats! You accessed a protected resource using a valid, non-blacklisted JWT.";
    }
}
