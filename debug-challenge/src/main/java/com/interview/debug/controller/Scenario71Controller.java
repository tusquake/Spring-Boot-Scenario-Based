package com.interview.debug.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/scenario71")
public class Scenario71Controller {

    @GetMapping(value = "/sensitive-page", produces = MediaType.TEXT_HTML_VALUE)
    public String getSensitivePage() {
        return "<html>" +
               "<head><title>Secure Banking Page</title></head>" +
               "<body style='font-family: Arial, sans-serif; padding: 50px; text-align: center; border: 5px solid red;'>" +
               "<h1>💰 Secure Transaction Page</h1>" +
               "<p>This is a highly sensitive page that should NEVER be embedded in another site.</p>" +
               "<button style='padding: 15px 30px; background-color: #d9534f; color: white; border: none; border-radius: 5px; cursor: pointer; font-size: 18px;'>" +
               "Transfer $10,000" +
               "</button>" +
               "<p style='margin-top: 20px; color: #666;'>If this page is embedded, an attacker could overlay a 'Click Here for Free Prizes' button over the Transfer button.</p>" +
               "</body>" +
               "</html>";
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Clickjacking scenario is active. Check /sensitive-page to test embedding.");
    }
}
