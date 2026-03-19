package com.interview.debug.controller;

import com.interview.debug.serialization.SafeObjectInputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Base64;
import java.util.Set;

@RestController
@RequestMapping("/api/scenario72")
public class Scenario72Controller {

    // A simple serializable class for demonstration
    public static class UserProfile implements Serializable {
        private static final long serialVersionUID = 1L;
        public String username;
        public String role;

        public UserProfile(String username, String role) {
            this.username = username;
            this.role = role;
        }

        @Override
        public String toString() {
            return "UserProfile{username='" + username + "', role='" + role + "'}";
        }
    }

    /**
     * VULNERABLE ENDPOINT
     * Directly deserializes Base64 data from the user.
     */
    @PostMapping("/vulnerable/deserialize")
    public ResponseEntity<String> vulnerableDeserialize(@RequestBody String base64Data) {
        try {
            byte[] data = Base64.getDecoder().decode(base64Data);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                Object obj = ois.readObject();
                return ResponseEntity.ok("Deserialized (Vulnerable): " + obj.toString());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * SECURE ENDPOINT
     * Uses SafeObjectInputStream with an allowlist.
     */
    @PostMapping("/secure/deserialize")
    public ResponseEntity<String> secureDeserialize(@RequestBody String base64Data) {
        try {
            byte[] data = Base64.getDecoder().decode(base64Data);
            Set<String> allowlist = Set.of(UserProfile.class.getName());
            
            try (SafeObjectInputStream ois = new SafeObjectInputStream(new ByteArrayInputStream(data), allowlist)) {
                Object obj = ois.readObject();
                return ResponseEntity.ok("Deserialized (Secure): " + obj.toString());
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Blocked: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/generate-payload")
    public ResponseEntity<String> generatePayload(@RequestParam String username) {
        try {
            UserProfile profile = new UserProfile(username, "USER");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(profile);
            }
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(bos.toByteArray()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating payload");
        }
    }
}
