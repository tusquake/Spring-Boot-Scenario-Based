package com.interview.debug.controller;

import com.interview.debug.service.Scenario116OrderedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Scenario 116: Order Inspector Controller
 */
@RestController
@RequestMapping("/api/scenario116")
public class Scenario116OrderController {

    private final List<Scenario116OrderedService> orderedServices;

    /**
     * Spring automatically injects all beans of type Scenario116OrderedService
     * into this List, sorted according to their @Order values.
     */
    public Scenario116OrderController(List<Scenario116OrderedService> orderedServices) {
        this.orderedServices = orderedServices;
    }

    @GetMapping("/beans")
    public List<String> getBeanOrder() {
        return orderedServices.stream()
                .map(Scenario116OrderedService::getBeanName)
                .collect(Collectors.toList());
    }
}
