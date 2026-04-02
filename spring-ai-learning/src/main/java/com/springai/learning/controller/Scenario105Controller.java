package com.springai.learning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenario105")
public class Scenario105Controller {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public Scenario105Controller(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
        this.chatClient = builder.build();
    }

    /**
     * Stateful chat endpoint.
     * Example:
     * /api/scenario105/chat?chatId=user1&message=My name is Batman
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String chatId,
            @RequestParam String message) {

        return chatClient.prompt()
                .user(message)
                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(chatId)
                                .build())
                .call() // ← executes the request
                .content(); // ← extracts the String response
    }

    /**
     * Clear memory for a specific chat session.
     * Example:
     * /api/scenario105/clear?chatId=user1
     */
    @GetMapping("/clear")
    public String clear(@RequestParam String chatId) {
        chatMemory.clear(chatId);
        return "Memory cleared for session: " + chatId;
    }
}