package com.interview.debug.model;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
public class Scenario2SingletonBean {
    private static final Logger logger = LoggerFactory.getLogger(Scenario2SingletonBean.class);
    private final String beanId = UUID.randomUUID().toString();

    @PostConstruct
    public void init() {
        logger.info("[Scenario 2] Singleton Bean Initialized. ID: {}", beanId);
    }
}
