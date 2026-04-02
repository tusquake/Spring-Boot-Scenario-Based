package com.springai.learning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SCENARIO 104: Prompt Templates & Roles
 * 
 * This scenario demonstrates how to separate prompt text from Java code
 * using external .st files and how to define AI personas using System Messages.
 */
@RestController
@RequestMapping("/api/scenario104")
public class Scenario104Controller {

    private final ChatClient chatClient;

    // Importing external templates as Resources
    @Value("classpath:/prompts/movie-critic.st")
    private Resource movieCriticResource;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessageResource;

    public Scenario104Controller(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Standard movie review using a template.
     * Usage: GET /api/scenario104/standard?movieName=Inception
     */
    @GetMapping("/standard")
    public String standardReview(@RequestParam String movieName) {
        return chatClient.prompt()
                .user(u -> u.text(movieCriticResource)
                            .param("movieName", movieName))
                .call()
                .content();
    }

    /**
     * Grumpy 1950s critic review using both a System Role and a Template.
     * Usage: GET /api/scenario104/grumpy?movieName=Avatar
     */
    @GetMapping("/grumpy")
    public String grumpyReview(@RequestParam String movieName) {
        return chatClient.prompt()
                .system(systemMessageResource) // Set the persona
                .user(u -> u.text(movieCriticResource)
                            .param("movieName", movieName))
                .call()
                .content();
    }
}
