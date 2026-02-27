package com.interview.debug.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReportService {

    // THE FIX: Instead of a manual HashMap, we use @Cacheable.
    // Spring handles the "caching" logic via the CacheManager (Caffeine/Redis).
    @Cacheable(value = "reports", key = "#userId")
    public String generateUserReport(String userId) {
        System.out.println("LOG: Generating slow report for user: " + userId);

        // Simulating heavy work
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        return "Report Contents for " + userId + " - Generated at: " + System.currentTimeMillis();
    }
}
