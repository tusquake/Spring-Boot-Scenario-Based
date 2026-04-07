package com.interview.debug.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Scenario 114: Remember Me Controller
 */
@RestController
@RequestMapping("/api/scenario114")
public class Scenario114Controller {

    @GetMapping("/show-login")
    public String showLoginByGet() {
        return "<html><body>" +
               "<h2>Scenario 114: Login</h2>" +
               "<form action='/debug-application/api/scenario114/login' method='POST'>" +
               "User: <input type='text' name='username' value='user114'><br>" +
               "Pass: <input type='password' name='password' value='password114'><br>" +
               "<input type='checkbox' name='remember-me'> Remember Me<br>" +
               "<input type='submit' value='Login'>" +
               "</form></body></html>";
    }

    @GetMapping("/protected")
    public Map<String, Object> protectedPage(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if (authentication == null) {
            response.put("status", "Not Authenticated");
            return response;
        }

        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        
        // Key Interview Concept: How to detect IF someone logged in via Remember Me vs Full Login?
        boolean isRememberMe = authentication instanceof RememberMeAuthenticationToken;
        response.put("authenticatedViaRememberMe", isRememberMe);
        response.put("message", isRememberMe ? 
            "Welcome back! You were logged in automatically." : 
            "You performed a full login.");
            
        return response;
    }
}
