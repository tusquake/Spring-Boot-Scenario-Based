package com.interview.debug.scenario130;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Scenario 130: Static Analysis Demo Controller.
 * This class contains intentional PMD and Checkstyle violations.
 */
@RestController
@RequestMapping("/api/scenario130")
public class StaticAnalysisController {

    // Checkstyle violation: Member name must match pattern (should be camelCase)
    private String User_Name = "Admin";

    @GetMapping("/check")
    public Map<String, Object> checkQuality(@RequestParam(required = false) String input) {
        // PMD violation: Unused local variable
        String unusedVar = "I am not used";

        // Checkstyle violation: WhitespaceAround (missing spaces around '=')
        int x=10;

        // PMD violation: Avoid deeply nested if statements
        if (input != null) {
            if (input.length() > 0) {
                if (input.startsWith("A")) {
                    if (input.endsWith("Z")) {
                        System.out.println("Deeply nested!");
                    }
                }
            }
        }

        // Checkstyle violation: Missing switch default
        switch (x) {
            case 10:
                System.out.println("Ten");
                break;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Analyzed");
        response.put("message", "Check the build logs for PMD and Checkstyle errors!");
        return response;
    }

    // PMD violation: Unused private method
    private void unnecessaryMethod() {
        System.out.println("I am never called");
    }
}
