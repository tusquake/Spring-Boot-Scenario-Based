package com.interview.debug.controller;

import com.interview.debug.model.Scenario127Product;
import com.interview.debug.repository.Scenario127ProductRepository;
import com.interview.debug.service.Scenario127Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Scenario 127: Caching Strategies Controller
 */
@RestController
@RequestMapping("/api/scenario127")
@RequiredArgsConstructor
@Slf4j
public class Scenario127Controller {

    private final Scenario127Service scenario127Service;
    private final Scenario127ProductRepository repository;

    @GetMapping("/cache-aside/{id}")
    public Scenario127Product getCacheAside(@PathVariable Long id) {
        return scenario127Service.getCacheAside(id);
    }

    @PostMapping("/write-through/{id}")
    public Scenario127Product postWriteThrough(@PathVariable Long id, @RequestParam String name) {
        return scenario127Service.updateWriteThrough(id, name);
    }

    @PostMapping("/write-behind/{id}")
    public Scenario127Product postWriteBehind(@PathVariable Long id, @RequestParam String name) {
        long startTime = System.currentTimeMillis();
        Scenario127Product response = scenario127Service.updateWriteBehind(id, name);
        long endTime = System.currentTimeMillis();
        
        log.info("⏱️ Write Behind response sent in {}ms", (endTime - startTime));
        return response;
    }

    /**
     * Helper to check the current state of DB vs Cache.
     */
    @GetMapping("/verify/{id}")
    public Map<String, Object> verify(@PathVariable Long id) {
        Scenario127Product dbProduct = repository.findById(id).orElse(null);
        // We call getCacheAside to see what's in the cache (it will hit cache first)
        Scenario127Product cacheProduct = scenario127Service.getCacheAside(id);

        return Map.of(
            "databaseValue", dbProduct != null ? dbProduct.getName() : "null",
            "cacheValue", cacheProduct != null ? cacheProduct.getName() : "null",
            "inSync", dbProduct != null && cacheProduct != null && dbProduct.getName().equals(cacheProduct.getName())
        );
    }
}
