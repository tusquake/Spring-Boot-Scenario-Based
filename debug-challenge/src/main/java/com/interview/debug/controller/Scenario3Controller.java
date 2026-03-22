package com.interview.debug.controller;

import com.interview.debug.service.Scenario3ConstructorService;
import com.interview.debug.service.Scenario3FieldService;
import com.interview.debug.service.Scenario3SetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario3")
@RequiredArgsConstructor
public class Scenario3Controller {

    private final Scenario3ConstructorService constructorService;
    private final Scenario3FieldService fieldService;
    private final Scenario3SetterService setterService;

    @GetMapping("/test")
    public Map<String, String> testInjections() {
        Map<String, String> results = new HashMap<>();
        
        results.put("constructor_injection", constructorService.execute());
        results.put("field_injection", fieldService.execute());
        results.put("setter_injection", setterService.execute());
        results.put("verdict", "All work technically, but Constructor Injection is the ONLY one that allows 'final' fields and is easiest to unit test.");

        return results;
    }
}
