package com.interview.debug.service;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * Scenario 116: Low Priority Bean
 * A higher @Order value indicates lower priority (Last in the list).
 */
@Service
@Order(2)
public class Scenario116BetaService implements Scenario116OrderedService {
    @Override
    public String getBeanName() {
        return "Beta (Order 2)";
    }
}
