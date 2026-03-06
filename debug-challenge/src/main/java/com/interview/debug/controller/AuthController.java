package com.interview.debug.controller;

import com.interview.debug.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username) {
        if ("admin".equals(username)) {
            String token = jwtUtil.generateToken(username);

            // THE FIX: Creating a secure, cross-domain cookie
            ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                    .httpOnly(true) // Prevents XSS (Javascript cannot read it)
                    .secure(true) // Required for SameSite=None
                    .path("/")
                    .maxAge(3600) // 1 hour
                    .sameSite("None") // REQUIRED for cross-domain cookie sharing
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Login successful, cookie set!");
        }
        return ResponseEntity.status(401).body("Invalid Credentials");
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // PRO TIP: Using StringUtils.hasText() is safer than checking null/empty
        // manually
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or Invalid Authorization Header");
        }

        // Safely extract token even if there are multiple spaces
        String token = authHeader.replace("Bearer ", "").trim();

        if (jwtUtil.validateToken(token)) {
            String user = jwtUtil.extractUsername(token);
            return ResponseEntity.ok("Token is valid for: " + user);
        } else {
            return ResponseEntity.status(401).body("Token is EXPIRED or INVALID!");
        }
    }
}
