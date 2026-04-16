package com.interview.debug.scenario136;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexingService {

    private final UserActivityRepository repository;
    private final JdbcTemplate jdbcTemplate;

    // REMOVED @Transactional to prevent transaction log bloat for 1 million records
    public void populateData(int count) {
        log.info("Starting population of {} records...", count);
        
        String sql = "INSERT INTO user_activities (tracking_id, activity_type, payload) VALUES (?, ?, ?)";
        
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            batchArgs.add(new Object[]{
                UUID.randomUUID().toString(),
                "LOG_EVENT",
                "{\"data\": \"some large payload for simulation\", \"index\": " + i + "}"
            });
            
            if (batchArgs.size() >= 1000) {
                jdbcTemplate.batchUpdate(sql, batchArgs);
                batchArgs.clear();
                
                // Log progress every 100,000 records
                if ((i + 1) % 100000 == 0) {
                    log.info("Progress: {} / {} records populated", (i + 1), count);
                }
            }
        }
        
        if (!batchArgs.isEmpty()) {
            jdbcTemplate.batchUpdate(sql, batchArgs);
        }
        
        log.info("Successfully populated all {} records.", count);
    }

    public SearchResult searchByTrackingId(String trackingId) {
        long startTime = System.nanoTime();
        
        var activity = repository.findByTrackingId(trackingId);
        
        long endTime = System.nanoTime();
        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        
        return new SearchResult(
            activity.isPresent(),
            durationMs,
            "Execution took " + durationMs + "ms"
        );
    }

    public record SearchResult(boolean found, long durationMs, String message) {}
}
