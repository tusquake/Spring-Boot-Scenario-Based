package com.interview.debug.controller;

import com.interview.debug.model.UserSecret;
import com.interview.debug.repository.SecretRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario13")
public class Scenario13Controller {

    private final SecretRepository secretRepository;

    public Scenario13Controller(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    @PostMapping("/save")
    public String saveSecret(@RequestParam String username, @RequestParam String data) {
        UserSecret secret = new UserSecret();
        secret.setUsername(username);
        secret.setSensitiveData(data);
        secretRepository.save(secret);
        return "Secret saved for " + username;
    }

    @GetMapping("/get/{username}")
    public Map<String, String> getSecret(@PathVariable String username) {
        UserSecret secret = secretRepository.findAll().stream()
                .filter(s -> s.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found"));

        // When we read from Repo, it's ALREADY decrypted by JPA
        String encryptedInDb = secretRepository.findRawSensitiveDataByUsername(username);

        Map<String, String> response = new HashMap<>();
        response.put("username", secret.getUsername());
        response.put("decryptedValueInsideJava", secret.getSensitiveData());
        response.put("rawEncryptedValueInDb", encryptedInDb);
        
        return response;
    }
}
