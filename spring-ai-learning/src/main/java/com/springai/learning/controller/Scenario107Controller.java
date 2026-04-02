package com.springai.learning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springai.learning.config.Scenario107Config;

/**
 * SCENARIO 107: AI Function Calling (Tool Use)
 * 
 * This scenario demonstrates how to give the AI "Hands" to call Java methods.
 * The AI will "identify" that it needs a tracking status and automatically
 * call the 'trackingFunction' bean.
 */
@RestController
@RequestMapping("/api/scenario107")
public class Scenario107Controller {

    private final ChatClient chatClient;

    // ✅ Inject the tool component directly
    public Scenario107Controller(ChatClient.Builder builder, Scenario107Config trackingTools) {
        this.chatClient = builder
                .defaultTools(trackingTools) // ✅ Pass the bean instance, not the string name
                .build();
    }

    @GetMapping("/track")
    public String track(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
