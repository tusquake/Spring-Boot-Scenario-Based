package com.interview.debug.controller;

import com.interview.debug.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username) {
        // Simulating authentication
        if ("admin".equals(username)) {
            return jwtUtil.generateToken(username);
        }
        return "Invalid Credentials";
    }

    @GetMapping("/validate")
    public String validate(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Invalid Header Format";
        }

        String token = authHeader.substring(7);

        if (jwtUtil.validateToken(token)) {
            return "Token is valid for user: " + jwtUtil.extractUsername(token);
        } else {
            return "Token is INVALID!";
        }
    }
}
