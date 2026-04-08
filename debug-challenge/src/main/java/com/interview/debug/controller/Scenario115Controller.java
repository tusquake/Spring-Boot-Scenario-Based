package com.interview.debug.controller;

import com.interview.debug.model.Scenario115UserForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scenario 115: @ModelAttribute Masterclass
 */
@RestController
@RequestMapping("/api/scenario115")
public class Scenario115Controller {

    /**
     * Pattern 1: Method-Level @ModelAttribute
     * This method is called BEFORE every handler method in this controller.
     * It ensures the "topics" attribute is always available in the Model.
     */
    @ModelAttribute("topics")
    public List<String> populateTopics() {
        return Arrays.asList("Spring Boot", "Spring Security", "JPA", "Kafka", "Cloud");
    }

    /**
     * Shows the current state of the Model (including pre-populated topics).
     * This demonstrates that 'topics' is present even if we didn't add it in this specific method.
     */
    @GetMapping("/form")
    public Map<String, Object> viewForm(Model model) {
        Map<String, Object> response = new HashMap<>();
        response.put("modelAttributes", model.asMap());
        response.put("usageMessage", "The 'topics' attribute is here because of the method-level @ModelAttribute.");
        return response;
    }

    /**
     * Pattern 2: Parameter-Level @ModelAttribute
     * This binds incoming form data (application/x-www-form-urlencoded) to the Java object.
     * Note: This is different from @RequestBody which handles JSON.
     */
    @PostMapping(value = "/submit", consumes = "application/x-www-form-urlencoded")
    public Map<String, Object> submitForm(@ModelAttribute Scenario115UserForm form) {
        Map<String, Object> response = new HashMap<>();
        response.put("receivedData", form);
        response.put("bindingSuccess", true);
        return response;
    }
}
