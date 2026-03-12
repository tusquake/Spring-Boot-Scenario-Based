package com.interview.debug.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // Cache for buckets (client identifier -> bucket)
    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey) {
        return bucketCache.computeIfAbsent(apiKey, this::createNewBucket);
    }

    private Bucket createNewBucket(String key) {
        // Define limit: 5 requests per minute using modern v8.x API
        Bandwidth limit = Bandwidth.builder()
                .capacity(5)
                .refillIntervally(5, Duration.ofMinutes(1))
                .build();
        
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
