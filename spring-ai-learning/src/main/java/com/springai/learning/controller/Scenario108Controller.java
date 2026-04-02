package com.springai.learning.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SCENARIO 108: AI Observability & Governance
 * 
 * This scenario covers:
 * 1. Token Tracking (Accounting)
 * 2. Metrics (Spring Boot Actuator)
 * 3. Data Redaction (Security/PII Filtering)
 */
@RestController
@RequestMapping("/api/scenario108")
public class Scenario108Controller {

    private final ChatClient chatClient;

    public Scenario108Controller(ChatClient.Builder builder) {
        // By default, ChatClient integrates with Micrometer Observations 
        // if an ObservationRegistry is present in the context.
        this.chatClient = builder.build();
    }

    /**
     * Tracked Chat Endpoint.
     * Returns the answer along with the "Receipt" (Token Usage).
     */
    @GetMapping("/chat")
    public Map<String, Object> chat(@RequestParam String message) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();

        Usage usage = response.getMetadata().getUsage();

        return Map.of(
            "answer", response.getResult().getOutput().getText(),
            "metrics", Map.of(
                "promptTokens", usage.getPromptTokens(),
                "completionTokens", usage.getCompletionTokens(),
                "totalTokens", usage.getTotalTokens(),
                "model", response.getMetadata().getModel()
            )
        );
    }

    /**
     * Redacted Chat Endpoint.
     * Demonstrates how to scrub PII (emails/numbers) before it's processed or logged.
     */
    @GetMapping("/redact")
    public String redact(@RequestParam String message) {
        // Here we demonstrate a simple redaction strategy using a custom advisor check.
        // In a real prod app, you would use a dedicated library like 'presidio' or a more robust regex-based advisor.
        String redactedMessage = message.replaceAll("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}", "[EMAIL_REDACTED]");
        
        System.out.println("🛡️ Original: " + message);
        System.out.println("🛡️ Redacted: " + redactedMessage);

        return chatClient.prompt()
                .user(redactedMessage)
                .call()
                .content();
    }
}
