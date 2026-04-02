package com.springai.learning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Streaming version of Scenario 102.
 *
 * Uses {@code ChatClient.stream()} to return partial AI responses as a Flux.
 * Clients can consume the stream with `curl -N` or an EventSource.
 */
@RestController
@RequestMapping("/api/scenario102")
public class Scenario102StreamingController {

    private final ChatClient chatClient;

    public Scenario102StreamingController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Stream AI response.
     * Example:
     * curl -N
     * "http://localhost:8081/spring-ai/api/scenario102/stream?prompt=Tell%20me%20a%20joke"
     */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(value = "prompt", defaultValue = "Hello") String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content(); // each emitted element is a partial chunk of the response
    }
}
