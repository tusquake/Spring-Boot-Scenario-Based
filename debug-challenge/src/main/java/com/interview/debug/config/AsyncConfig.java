package com.interview.debug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // CRITICAL: This enables @Async support
public class AsyncConfig {

    @Bean(name = "backgroundTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // --- Best Practices for Thread Pools ---
        
        executor.setCorePoolSize(5);        // Minimum threads to keep alive
        executor.setMaxPoolSize(10);       // Max threads if queue is full
        executor.setQueueCapacity(50);      // Max items in queue before creating new threads
        executor.setThreadNamePrefix("Async-"); // Prefix for easier debugging in logs
        executor.initialize();
        
        return executor;
    }
}
