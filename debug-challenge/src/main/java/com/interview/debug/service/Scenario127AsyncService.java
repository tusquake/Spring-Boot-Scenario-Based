package com.interview.debug.service;

import com.interview.debug.model.Scenario127Product;
import com.interview.debug.repository.Scenario127ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Helper service to handle @Async operations for Scenario 127.
 * Separated to avoid proxy self-invocation issues.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class Scenario127AsyncService {

    private final Scenario127ProductRepository repository;

    @Async("backgroundTaskExecutor")
    public void saveToDbAsync(Scenario127Product product) {
        log.info("⏳ [ASYNC] Background task started for ID: {}", product.getId());
        try {
            Thread.sleep(5000); // Simulate slow DB update (5s)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        repository.save(product);
        log.info("✅ [ASYNC] Background DB update COMPLETED for ID: {}", product.getId());
    }
}
