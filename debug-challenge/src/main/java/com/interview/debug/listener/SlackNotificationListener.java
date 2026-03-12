package com.interview.debug.listener;

import com.interview.debug.event.UserRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SlackNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(SlackNotificationListener.class);

    @Async
    @EventListener
    public void handleUserRegistration(UserRegistrationEvent event) throws InterruptedException {
        String username = event.getUsername();
        logger.info("[Async-Slack] Start posting registration alert for {} to Slack...", username);
        
        // Simulate API delay
        Thread.sleep(2000);
        
        logger.info("[Async-Slack] Slack notification posted for {}!", username);
    }
}
