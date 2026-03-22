package com.interview.debug.model;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope("prototype")
@Getter
public class Scenario2PrototypeBean {
    private static final Logger logger = LoggerFactory.getLogger(Scenario2PrototypeBean.class);
    private final String beanId = UUID.randomUUID().toString();

    @PostConstruct
    public void init() {
        logger.info("[Scenario 2] Prototype Bean Created! ID: {}", beanId);
    }
}
