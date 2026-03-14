package com.interview.debug.scheduler;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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
         * FIXED RATE WITH SHEDLOCK:
         * Even if you run 10 instances of this app, only ONE will run this task
         * because it checks the 'shedlock' table first.
         */
        @Scheduled(fixedRate = 100000000)
        @SchedulerLock(name = "fixedRateTaskLock", lockAtMostFor = "1m", lockAtLeastFor = "5s")
        public void fixedRateTask() {
                logger.info("[{}] ⏰ FIXED RATE [10s] -> Execution Time: {}",
                                Thread.currentThread().getName(), dtf.format(LocalDateTime.now()));
        }

        /**
         * FIXED DELAY: Runs 10 seconds AFTER the previous execution finishes.
         */
        @Scheduled(fixedDelay = 1000000000)
        public void fixedDelayTask() throws InterruptedException {
                logger.info("[{}] ⏸️ FIXED DELAY [10s] -> Starting work...",
                                Thread.currentThread().getName());
                Thread.sleep(2000);
                logger.info("[{}] ✅ FIXED DELAY [10s] -> Finished work.",
                                Thread.currentThread().getName());
        }

        /**
         * HEAVY TASK: This task runs every 5 seconds but takes 15 seconds to finish!
         * With a thread pool, this won't block the FIXED RATE task from firing.
         */
        @Scheduled(fixedRate = 50000000)
        public void heavyConcurrentTask() throws InterruptedException {
                logger.info("[{}] 🏋️ HEAVY START -> Time: {}",
                                Thread.currentThread().getName(), dtf.format(LocalDateTime.now()));
                Thread.sleep(15000); // Take 15s
                logger.info("[{}] 🏋️ HEAVY END -> Time: {}",
                                Thread.currentThread().getName(), dtf.format(LocalDateTime.now()));
        }

        // @Scheduled(cron = "0 * * * * *")
        // public void cronTask() {
        // logger.info("[{}] 🗓️ CRON TASK -> Top of the minute: {}",
        // Thread.currentThread().getName(), dtf.format(LocalDateTime.now()));
        // }
}
