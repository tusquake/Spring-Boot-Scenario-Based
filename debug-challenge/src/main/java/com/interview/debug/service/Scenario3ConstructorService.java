package com.interview.debug.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * BEST PRACTICE: Constructor Injection
 * - Final fields ensure immutability.
 * - Easy to unit test (no reflection needed).
 * - Mandatory dependencies are clear.
 */
@Service
@RequiredArgsConstructor
public class Scenario3ConstructorService {
    private final Scenario3DataService dataService;

    public String execute() {
        return "Constructor Service -> " + dataService.getData();
    }
}
