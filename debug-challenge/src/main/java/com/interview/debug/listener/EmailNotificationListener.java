package com.interview.debug.listener;

import com.interview.debug.event.UserRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationListener.class);

    @Async
    @EventListener
    public void handleUserRegistration(UserRegistrationEvent event) throws InterruptedException {
        String username = event.getUsername();
        logger.info("[Async-Email] Start sending welcome email to {}...", username);
        
        // Simulate network delay
        Thread.sleep(3000);
        
        logger.info("[Async-Email] Welcome email sent successfully to {}!", username);
    }
}
