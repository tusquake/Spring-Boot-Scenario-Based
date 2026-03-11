package com.interview.debug.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> processAsyncTask() {
        System.out.println("Async Task started on thread: " + Thread.currentThread().getName());
        
        // Check if Security Context is available
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : "ANONYMOUS";
        
        System.out.println("Async Task: Current user is: " + user);
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        return CompletableFuture.completedFuture("Async Task completed for user: " + user);
    }
}
