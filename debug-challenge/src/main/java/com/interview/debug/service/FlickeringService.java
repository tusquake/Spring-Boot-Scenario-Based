package com.interview.debug.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FlickeringService {
    private static final Logger logger = LoggerFactory.getLogger(FlickeringService.class);
    private final AtomicInteger attempts = new AtomicInteger(0);

    /**
     * Demonstrate Retry with Exponential Backoff and Jitter.
     * delay = 1s, multiplier = 2 (exponential: 1s, 2s, 4s...)
     * random = true (jitter)
     */
    @Retryable(
        retryFor = { RuntimeException.class },
        maxAttempts = 4,
        backoff = @Backoff(delay = 1000, multiplier = 2.0, random = true)
    )
    public String callExternalApi() {
        int currentAttempt = attempts.incrementAndGet();
        logger.info("📡 Attempt {} -> Calling External API...", currentAttempt);

        if (currentAttempt < 4) {
             logger.warn("⚠️ Attempt {} FAILED: Simulation of transient network error.", currentAttempt);
             throw new RuntimeException("External API is temporarily down!");
        }

        logger.info("✅ Attempt {} SUCCESS!", currentAttempt);
        attempts.set(0); // Reset for next demo run
        return "SUCCESS after 4 attempts.";
    }

    @Recover
    public String recover(RuntimeException e) {
        logger.error("🛑 ALL RETRY ATTEMPTS FAILED. Executing fallback logic...");
        attempts.set(0);
        return "FALLBACK: System is still down. Returning cached/default data.";
    }
}
