package com.interview.debug.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class Scenario124MemoryLeakService {

    // Classic memory leak: a static collection that grows indefinitely
    private static final Map<String, byte[]> LEAK_CACHE = new HashMap<>();

    /**
     * Simulates a memory leak by adding large byte arrays to a static map.
     * Each call adds 'count' items of 1MB each.
     */
    public void leakMemory(int count) {
        log.warn("🚨 Leaking {} MB into the static cache...", count);
        for (int i = 0; i < count; i++) {
            // Allocate 1MB
            byte[] largeData = new byte[1024 * 1024];
            // Store it with a unique key
            LEAK_CACHE.put(UUID.randomUUID().toString(), largeData);
        }
        log.info("Leak complete. Total items in cache: {}", LEAK_CACHE.size());
    }

    public int getCacheSize() {
        return LEAK_CACHE.size();
    }

    public void clearMemory() {
        log.info("🧹 Clearing the leak cache as requested.");
        LEAK_CACHE.clear();
        // Request GC to show recovery in monitoring tools (though not guaranteed)
        System.gc();
    }
}
