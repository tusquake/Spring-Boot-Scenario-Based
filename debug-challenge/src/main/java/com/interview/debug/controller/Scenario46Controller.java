package com.interview.debug.controller;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario46")
public class Scenario46Controller {

    private final RedisConnectionFactory connectionFactory;

    public Scenario46Controller(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @GetMapping("/status")
    public Map<String, Object> getRedisStatus() {
        try {
            boolean isConnected = !connectionFactory.getConnection().isClosed();
            return Map.of(
                "connected", isConnected,
                "service", "Redis (Managed by Docker Compose)",
                "note", "Zero-config connection established automatically!"
            );
        } catch (Exception e) {
            return Map.of(
                "connected", false,
                "error", e.getMessage(),
                "note", "Ensure Docker is running on your machine."
            );
        }
    }
}
