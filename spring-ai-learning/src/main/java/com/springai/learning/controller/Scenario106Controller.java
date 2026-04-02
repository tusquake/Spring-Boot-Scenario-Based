package com.springai.learning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario106")
public class Scenario106Controller {

    private final ChatClient chatClient;

    public Scenario106Controller(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore).build() // ✅ Use builder
                )
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}