package com.interview.debug.controller;

import com.interview.debug.model.Scenario101Model;
import com.interview.debug.repository.Scenario101Client;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Scenario 101: Declarative HTTP Clients (@HttpExchange).
 * Controller to demonstrate calling an external API via an interface proxy.
 */
@RestController
@RequestMapping("/api/scenario101")
@RequiredArgsConstructor
public class Scenario101Controller {

    private final Scenario101Client postClient;

    @GetMapping("/posts")
    public ResponseEntity<List<Scenario101Model>> getAllPosts() {
        return ResponseEntity.ok(postClient.getAllPosts());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Scenario101Model> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postClient.getPostById(id));
    }

    @PostMapping("/posts")
    public ResponseEntity<Scenario101Model> createPost(@RequestBody Scenario101Model post) {
        // Mocking the creation by sending a post
        return ResponseEntity.ok(postClient.createPost(post));
    }
}
