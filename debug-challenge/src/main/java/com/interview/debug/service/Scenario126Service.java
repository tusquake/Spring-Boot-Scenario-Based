package com.interview.debug.service;

import com.interview.debug.model.Scenario126Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Scenario 126: Cache Stampede Problem & Prevention
 * 
 * Demonstrates how multiple concurrent requests for the same expired cache key
 * can overwhelm the database (Stampede), and how to prevent it using sync=true.
 */
@Service
@Slf4j
public class Scenario126Service {

    private final AtomicInteger databaseHits = new AtomicInteger(0);

    /**
     * VULNERABLE METHOD: Standard @Cacheable
     * When the cache expires, multiple threads calling this simultaneously
     * will all pass through to the 'expensive' method logic.
     */
    @Cacheable(value = "scenario126_products", key = "#id")
    public Scenario126Product getProductVulnerable(String id) {
        return simulateDatabaseCall(id, "VULNERABLE");
    }

    /**
     * SECURED METHOD: @Cacheable(sync = true)
     * When the cache expires, only ONE thread is allowed to execute this method.
     * All other concurrent threads for the same key will block until the
     * result is available in the cache.
     */
    @Cacheable(value = "scenario126_products", key = "#id", sync = true)
    public Scenario126Product getProductSecured(String id) {
        return simulateDatabaseCall(id, "SECURED");
    }

    private Scenario126Product simulateDatabaseCall(String id, String mode) {
        int hitCount = databaseHits.incrementAndGet();
        log.info("🗄️ [{}] Database Hit #{} for Product ID: {} - Mode: {}", 
                 Thread.currentThread().getName(), hitCount, id, mode);

        // Simulate expensive database query or computation
        try {
            Thread.sleep(2000); // 2 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during simulation", e);
        }

        return Scenario126Product.builder()
                .id(id)
                .name("High Demand Product " + id)
                .price(new BigDecimal("99.99"))
                .description("Sample data for cache stampede demo (" + mode + ")")
                .build();
    }

    public int getDatabaseHits() {
        return databaseHits.get();
    }

    @CacheEvict(value = "scenario126_products", allEntries = true)
    public void resetAll() {
        log.info("🔄 Resetting database hits and clearing scenario126_products cache");
        databaseHits.set(0);
    }
}
