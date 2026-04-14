package com.interview.debug.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.interview.debug.model.Scenario128Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Scenario 128: Caffeine Caching Service
 */
@Service
@Slf4j
public class Scenario128Service {

    /**
     * Cache with 10s TTL
     */
    @Cacheable(value = "reports_ttl", cacheManager = "scenario128TtlCacheManager")
    public Scenario128Report getExpiringReport(String id) {
        log.info("📊 [TTL CACHE] Generating expensive report for ID: {}", id);
        return Scenario128Report.builder()
                .id(id)
                .content("Expensive Report Content")
                .generatedAt(LocalDateTime.now())
                .cacheSource("Database (Simulated)")
                .build();
    }

    /**
     * Cache with Max Size 2
     */
    @Cacheable(value = "reports_size", cacheManager = "scenario128SizeCacheManager")
    public Scenario128Report getSizeLimitedReport(String id) {
        log.info("📏 [SIZE CACHE] Generating expensive report for ID: {}", id);
        return Scenario128Report.builder()
                .id(id)
                .content("Size Limited Content")
                .generatedAt(LocalDateTime.now())
                .cacheSource("Database (Simulated)")
                .build();
    }

    private final CacheManager ttlCacheManager;
    private final CacheManager sizeCacheManager;

    public Scenario128Service(
            @Qualifier("scenario128TtlCacheManager") CacheManager ttlCacheManager,
            @Qualifier("scenario128SizeCacheManager") CacheManager sizeCacheManager) {
        this.ttlCacheManager = ttlCacheManager;
        this.sizeCacheManager = sizeCacheManager;
    }

    public Map<String, Object> getCacheStats() {
        Map<String, Object> statsMap = new HashMap<>();
        statsMap.put("ttlCache", formatStats("reports_ttl", ttlCacheManager));
        statsMap.put("sizeCache", formatStats("reports_size", sizeCacheManager));
        return statsMap;
    }

    private Map<String, Object> formatStats(String cacheName, CacheManager manager) {
        CaffeineCache caffeineCache = (CaffeineCache) manager.getCache(cacheName);
        if (caffeineCache == null) return Map.of("error", "Cache not initialized");

        Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
        CacheStats stats = nativeCache.stats();

        Map<String, Object> details = new HashMap<>();
        details.put("hitCount", stats.hitCount());
        details.put("missCount", stats.missCount());
        details.put("hitRate", stats.hitRate());
        details.put("evictionCount", stats.evictionCount());
        details.put("approxSize", nativeCache.estimatedSize());

        return details;
    }
}
