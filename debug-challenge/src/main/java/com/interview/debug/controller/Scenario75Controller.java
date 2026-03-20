package com.interview.debug.controller;

import com.interview.debug.dto.UserRegistrationDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scenario75")
public class Scenario75Controller {

    private static final Logger log = LoggerFactory.getLogger(Scenario75Controller.class);
    
    private final com.interview.debug.repository.Scenario75UserRepository userRepository;

    public Scenario75Controller(com.interview.debug.repository.Scenario75UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO, BindingResult bindingResult) {
        
        // Manual business check Example: Conditional logic
        if ("ADMIN".equalsIgnoreCase(registrationDTO.getRole()) && 
            (registrationDTO.getAdminKey() == null || registrationDTO.getAdminKey().isEmpty())) {
            bindingResult.rejectValue("adminKey", "AdminKey.required", "Admin key is required for ADMIN role");
        }

        if (bindingResult.hasErrors()) {
            log.warn("Validation failed for user registration: {}", registrationDTO.getEmail());
            
            String fieldErrors = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
                
            String globalErrors = bindingResult.getGlobalErrors().stream()
                .map(error -> error.getObjectName() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
                
            String allErrors = fieldErrors + (fieldErrors.isEmpty() || globalErrors.isEmpty() ? "" : ", ") + globalErrors;
            
            return ResponseEntity.badRequest().body("Validation Failed: " + allErrors);
        }

        // PERSISTENCE: Must save the user for UniqueEmail validation to work next time!
        com.interview.debug.model.Scenario75User newUser = new com.interview.debug.model.Scenario75User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(registrationDTO.getPassword()); // In real app, encode this!
        newUser.setRole(registrationDTO.getRole());
        userRepository.save(newUser);

        log.info("User registered and saved successfully: {}", registrationDTO.getUsername());
        return ResponseEntity.ok("Registration Successful for: " + registrationDTO.getUsername());
    }
}
