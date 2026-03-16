package com.interview.debug.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario58")
public class Scenario58Controller {

    private final PasswordEncoder delegatingEncoder;

    public Scenario58Controller(PasswordEncoder passwordEncoder) {
        this.delegatingEncoder = passwordEncoder;
    }

    /**
     * Demonstrates Configurable Encoding using DelegatingPasswordEncoder.
     * Use ?algorithm=bcrypt or ?algorithm=argon2 or ?algorithm=noop
     */
    @GetMapping("/encode")
    public Map<String, String> encode(
            @RequestParam String password,
            @RequestParam(defaultValue = "argon2") String algorithm) {
        
        String encoded;
        // In a real app, DelegatingPasswordEncoder handles this automatically based on storage.
        // Here we manually pick the encoder to show the user the different prefixes.
        if ("bcrypt".equalsIgnoreCase(algorithm)) {
            encoded = "{bcrypt}" + new BCryptPasswordEncoder().encode(password);
        } else if ("noop".equalsIgnoreCase(algorithm)) {
            encoded = "{noop}" + password;
        } else {
            encoded = "{argon2}" + Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().encode(password);
        }

        return Map.of(
            "algorithmRequested", algorithm,
            "encodedPassword", encoded,
            "note", "The prefix in {brackets} tells Spring which algorithm to use during verification!"
        );
    }

    @GetMapping("/public/info")
    public Map<String, String> publicInfo() {
        return Map.of("status", "This is public information.");
    }

    @GetMapping("/admin/secret")
    public Map<String, String> adminSecret() {
        return Map.of("secret", "This is a highly confidential admin secret!");
    }
}
