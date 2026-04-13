package com.interview.debug.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class Scenario123DeadlockService {

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void triggerDeadlock() {
        log.info("Initiating deadlock simulation...");

        // Thread 1
        CompletableFuture.runAsync(() -> {
            String threadName = "Deadlock-Thread-Alpha";
            Thread.currentThread().setName(threadName);
            synchronized (lock1) {
                log.info("{} acquired lock1. Waiting to acquire lock2...", threadName);
                try {
                    // Holding lock1 for 1 second to give Thread 2 time to acquire lock2
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log.info("{} attempting to acquire lock2...", threadName);
                synchronized (lock2) {
                    log.info("{} successfully acquired lock2!", threadName);
                }
            }
        });

        // Thread 2
        CompletableFuture.runAsync(() -> {
            String threadName = "Deadlock-Thread-Beta";
            Thread.currentThread().setName(threadName);
            synchronized (lock2) {
                log.info("{} acquired lock2. Waiting to acquire lock1...", threadName);
                try {
                    // Holding lock2 for 1 second to give Thread 1 time to acquire lock1
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log.info("{} attempting to acquire lock1...", threadName);
                synchronized (lock1) {
                    log.info("{} successfully acquired lock1!", threadName);
                }
            }
        });
    }
}
