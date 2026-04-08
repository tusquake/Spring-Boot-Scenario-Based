package com.interview.debug.service;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * Scenario 116: High Priority Bean
 * A lower @Order value indicates higher priority (First in the list).
 */
@Service
@Order(1)
public class Scenario116AlphaService implements Scenario116OrderedService {
    @Override
    public String getBeanName() {
        return "Alpha (Order 1)";
    }
}
