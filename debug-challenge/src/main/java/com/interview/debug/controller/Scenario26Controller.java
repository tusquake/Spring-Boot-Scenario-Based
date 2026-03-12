package com.interview.debug.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/scenario26")
public class Scenario26Controller {

    /**
     * Mono: Represents a single (or empty) non-blocking result.
     * Use case: Fetching a single user by ID from a reactive DB.
     */
    @GetMapping("/mono")
    public Mono<String> getSingleResult() {
        return Mono.just("Hello from the Reactive World!")
                .delayElement(Duration.ofMillis(500)); // Simulate non-blocking delay
    }

    /**
     * Flux: Represents a stream of 0 to N elements.
     * Use case: Real-time stock prices, twitter feeds, or large data exports.
     * Note: Produces TEXT_EVENT_STREAM (Server-Sent Events) so you can see it arrive in real-time.
     */
    @GetMapping(value = "/flux-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getStream() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1)) // Emit one number every second
                .log(); // Log internal signals (onNext, onComplete, etc.)
    }
}
