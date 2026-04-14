package com.interview.debug.service;

import com.interview.debug.model.Scenario127Product;
import com.interview.debug.repository.Scenario127ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Scenario 127: Caching Strategies
 * 
 * Demonstrates:
 * 1. Cache Aside (Lazy Loading)
 * 2. Write Through (Sync Update)
 * 3. Write Behind (Async Update)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class Scenario127Service {

    private final Scenario127ProductRepository repository;
    private final CacheManager cacheManager;
    private final Scenario127AsyncService asyncService;

    /**
     * 1. CACHE ASIDE (Lazy Loading)
     * Check cache first. If hit, return. If miss, fetch from DB and cache it.
     */
    @Cacheable(value = "scenario127_products", key = "#id")
    public Scenario127Product getCacheAside(Long id) {
        log.info("🔍 [CACHE ASIDE] Cache Miss! Fetching from DB for ID: {}", id);
        return repository.findById(id).orElse(null);
    }

    /**
     * 2. WRITE THROUGH (Synchronous)
     * Update DB and Cache simultaneously.
     * Response is blocked until DB update is complete.
     */
    @CachePut(value = "scenario127_products", key = "#id")
    @Transactional
    public Scenario127Product updateWriteThrough(Long id, String newName) {
        log.info("💾 [WRITE THROUGH] Updating DB and Cache synchronously for ID: {}", id);
        
        Scenario127Product product = repository.findById(id)
                .orElse(Scenario127Product.builder().id(id).build());
        
        product.setName(newName);
        product.setLastUpdated(LocalDateTime.now());
        
        // Synchronous DB save
        return repository.save(product);
    }

    /**
     * 3. WRITE BEHIND (Asynchronous)
     * Update Cache immediately and return.
     * DB is updated in the background.
     */
    public Scenario127Product updateWriteBehind(Long id, String newName) {
        log.info("⚡ [WRITE BEHIND] Updating Cache immediately. DB update scheduled asynchronously.");
        
        Scenario127Product product = Scenario127Product.builder()
                .id(id)
                .name(newName)
                .lastUpdated(LocalDateTime.now())
                .build();
        
        // Manually update the cache
        if (cacheManager.getCache("scenario127_products") != null) {
            cacheManager.getCache("scenario127_products").put(id, product);
        }

        // Trigger asynchronous DB update using the separate service
        asyncService.saveToDbAsync(product);

        return product;
    }
}
