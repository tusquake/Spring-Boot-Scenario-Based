package com.interview.debug.controller;

import com.interview.debug.model.Scenario111Post;
import com.interview.debug.service.Scenario111PostClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import com.interview.debug.service.Scenario111PostFeignClient;

/**
 * Scenario 111: External API Client Comparison
 * This controller demonstrates the 5 primary ways to call an external API in Spring Boot.
 */
@RestController
@RequestMapping("/api/scenario111")
@RequiredArgsConstructor
@Tag(name = "Scenario 111: External Clients", description = "Comparison of four ways to call external REST APIs.")
public class Scenario111ClientController {

    private static final String EXTERNAL_URL = "https://jsonplaceholder.typicode.com/posts/{id}";

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final RestClient restClient;
    private final Scenario111PostClient declarativeClient;
    private final Scenario111PostFeignClient feignClient;

    /**
     * 1. RestTemplate (Legacy Sync)
     */
    @GetMapping("/rest-template/{id}")
    @Operation(summary = "Using RestTemplate", description = "The classic, synchronous way to call an API.")
    public Scenario111Post getWithRestTemplate(@PathVariable Integer id) {
        return restTemplate.getForObject(EXTERNAL_URL, Scenario111Post.class, id);
    }

    /**
     * 2. WebClient (Modern Reactive)
     */
    @GetMapping("/web-client/{id}")
    @Operation(summary = "Using WebClient", description = "The reactive, non-blocking way (supports both sync and async).")
    public Scenario111Post getWithWebClient(@PathVariable Integer id) {
        return webClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .bodyToMono(Scenario111Post.class)
                .block(); // Blocking for synchronous demonstration
    }

    /**
     * 3. RestClient (Modern Fluent Sync)
     * Introduced in Spring 6.1 (Spring Boot 3.2).
     */
    @GetMapping("/rest-client/{id}")
    @Operation(summary = "Using RestClient", description = "The new fluent synchronous client (clean replacement for RestTemplate).")
    public Scenario111Post getWithRestClient(@PathVariable Integer id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Scenario111Post.class);
    }

    /**
     * 4. Declarative Interface (@HttpExchange)
     * Introduced in Spring 6. Cleanest code, similar to Spring Data JPA interfaces.
     */
    @GetMapping("/http-interface/{id}")
    @Operation(summary = "Using @HttpExchange", description = "The declarative approach where you only define the interface.")
    public Scenario111Post getWithDeclarative(@PathVariable Integer id) {
        return declarativeClient.getPostById(id);
    }

    /**
     * 5. OpenFeign (Spring Cloud)
     * The popular declarative approach in microservices.
     */
    @GetMapping("/feign/{id}")
    @Operation(summary = "Using OpenFeign", description = "The standard declarative client in the Spring Cloud ecosystem.")
    public Scenario111Post getWithFeign(@PathVariable Integer id) {
        return feignClient.getPostById(id);
    }
}
