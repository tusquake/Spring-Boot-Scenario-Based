package com.interview.debug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Scenario 112: Asynchronous Configuration
 * Demonstrates how to define a custom TaskExecutor.
 * 
 * CRITICAL INTERVIEW TIP:
 * By default, Spring uses a SimpleAsyncTaskExecutor which does NOT reuse threads.
 * It creates a new thread for every task, leading to OutOfMemory (OOM) errors in production.
 * Always define a ThreadPoolTaskExecutor with a fixed capacity.
 */
@Configuration
public class Scenario112AsyncConfig {

    @Bean(name = "scenario112Executor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);        // Minimum threads to keep alive
        executor.setMaxPoolSize(10);       // Maximum threads if queue is full
        executor.setQueueCapacity(25);     // Number of tasks to buffer before creating more threads
        executor.setThreadNamePrefix("Scenario112-");
        executor.initialize();
        return executor;
    }
}
