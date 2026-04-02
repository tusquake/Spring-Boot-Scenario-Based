package com.springai.learning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SCENARIO 102: The First Handshake (New Project Edition)
 * 
 * This controller demonstrates the basic usage of the Spring AI ChatClient
 * to connect with Google's Gemini AI.
 */
@RestController
@RequestMapping("/api/scenario102")
public class Scenario102Controller {

    private final ChatClient chatClient;

    // The ChatClient.Builder is auto-configured by the Spring AI starter
    public Scenario102Controller(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Basic endpoint to test AI connectivity.
     * Usage: GET /api/scenario102/ask?prompt=Your Question
     */
    @GetMapping("/ask")
    public String ask(@RequestParam(value = "prompt", defaultValue = "Tell me a joke about Java developers") String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
