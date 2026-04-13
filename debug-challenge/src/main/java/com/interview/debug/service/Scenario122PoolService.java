package com.interview.debug.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
public class Scenario122PoolService {

    private final DataSource dataSource;

    /**
     * Simulates a heavy database operation by holding a connection open.
     * In a real app, this might be a complex query or batch update.
     * WARNING: Sleeping inside @Transactional is an anti-pattern.
     */
    @Transactional
    public void simulateSlowQuery(int durationMs) {
        log.info("Starting slow query simulation for {}ms", durationMs);
        try {
            // We sleep here to hold the connection from the pool
            Thread.sleep(durationMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Simulation interrupted", e);
        }
        log.info("Slow query simulation completed");
    }

    /**
     * Simulates a connection leak by opening a connection and never closing it.
     * This will trigger Hikari's leak detection threshold if configured.
     */
    public void simulateLeak() {
        log.warn("🚨 Simulating a connection leak. This connection will NOT be closed.");
        try {
            // Using raw DataSource to get a connection and NOT using try-with-resources or close()
            Connection connection = dataSource.getConnection();
            log.info("Leaked connection acquired: {}", connection);
            // purposefully doing nothing with 'connection' and NOT closing it
        } catch (SQLException e) {
            log.error("Failed to acquire connection for leak simulation", e);
        }
    }
}
