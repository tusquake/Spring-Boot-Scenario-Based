package com.interview.debug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    /**
     * By default, Spring uses a single-threaded scheduler.
     * We override it with a ThreadPoolTaskScheduler to allow concurrent execution.
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // Allow 5 tasks to run at the same time
        scheduler.setThreadNamePrefix("scheduling-pool-");
        scheduler.initialize();
        return scheduler;
    }
}
