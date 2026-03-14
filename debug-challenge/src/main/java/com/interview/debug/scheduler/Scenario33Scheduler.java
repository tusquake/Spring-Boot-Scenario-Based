package com.interview.debug.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Scenario33Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(Scenario33Scheduler.class);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * FIXED RATE: Runs every 10 seconds.
     * The "Indifferent Alarm" - it doesn't care if the previous task is still running.
     * (Note: By default, Spring uses a single-threaded task scheduler, 
     * so it still waits unless you configure a ThreadPoolTaskScheduler).
     */
    @Scheduled(fixedRate = 10000)
    public void fixedRateTask() {
        logger.info("⏰ FIXED RATE [10s] -> Execution Time: {}", dtf.format(LocalDateTime.now()));
    }

    /**
     * FIXED DELAY: Runs 10 seconds AFTER the previous execution finishes.
     * The "Polite Alarm" - it waits for you to finish your work before starting the timer.
     */
    @Scheduled(fixedDelay = 10000)
    public void fixedDelayTask() throws InterruptedException {
        logger.info("⏸️ FIXED DELAY [10s] -> Starting work at: {}", dtf.format(LocalDateTime.now()));
        // Simulate some work
        Thread.sleep(2000);
        logger.info("✅ FIXED DELAY [10s] -> Finished work at: {}", dtf.format(LocalDateTime.now()));
    }

    /**
     * CRON EXPRESSION: Runs at the start of every minute (0th second).
     * Format: sec min hour day month weekday
     * The "Fancy Calendar Alarm".
     */
    @Scheduled(cron = "0 * * * * *")
    public void cronTask() {
        logger.info("🗓️ CRON TASK -> Running at the top of the minute: {}", dtf.format(LocalDateTime.now()));
    }
}
