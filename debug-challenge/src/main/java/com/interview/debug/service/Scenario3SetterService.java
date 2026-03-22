package com.interview.debug.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OPTIONAL: Setter Injection
 * - Good for optional dependencies.
 * - Beans can be reconfigured after initialization.
 */
@Service
public class Scenario3SetterService {
    private Scenario3DataService dataService;

    @Autowired
    public void setDataService(Scenario3DataService dataService) {
        this.dataService = dataService;
    }

    public String execute() {
        return "Setter Service -> " + dataService.getData();
    }
}
