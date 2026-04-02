package com.springai.learning.controller;

import com.springai.learning.dto.MovieInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SCENARIO 103: Structured Outputs (Mapping AI to Java POJOs)
 * 
 * This controller demonstrates how to get the AI to return structured data
 * and map it directly to a Java record (MovieInfo).
 */
@RestController
@RequestMapping("/api/scenario103")
public class Scenario103Controller {

    private final ChatClient chatClient;

    public Scenario103Controller(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Endpoint to fetch movie details as a structured JSON object.
     * Usage: GET /api/scenario103/movie?name=Inception
     */
    @GetMapping("/movie")
    public MovieInfo movieDetails(@RequestParam(value = "name", defaultValue = "Inception") String movieName) {
        return chatClient.prompt()
                .user(u -> u.text("Give me details about the movie {movieName}")
                        .param("movieName", movieName))
                .call()
                .entity(MovieInfo.class); // Automatically parses response into MovieInfo
    }
}
