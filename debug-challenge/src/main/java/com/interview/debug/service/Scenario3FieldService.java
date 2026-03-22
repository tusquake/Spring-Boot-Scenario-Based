package com.interview.debug.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * NOT RECOMMENDED: Field Injection
 * - Hidden dependencies.
 * - Impossibility to use 'final' keyword.
 * - Hard to unit test (requires reflection/Mockito).
 */
@Service
public class Scenario3FieldService {
    @Autowired
    private Scenario3DataService dataService;

    public String execute() {
        return "Field Service -> " + dataService.getData();
    }
}
