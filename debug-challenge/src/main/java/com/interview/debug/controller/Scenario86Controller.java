package com.interview.debug.controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/scenario86")
public class Scenario86Controller {

    // 1. Standard Path Variable (Required by default)
    // URL: /api/scenario86/users/105
    @GetMapping("/users/{id}")
    public Map<String, Object> getUserById(@PathVariable("id") Long userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "REQUIRED_PATH_VARIABLE");
        response.put("extractedId", userId);
        return response;
    }

    // 2. Optional Path Variable (using Optional)
    // URL: /api/scenario86/details OR /api/scenario86/details/tech
    @GetMapping({"/details", "/details/{type}"})
    public Map<String, Object> getDetails(@PathVariable("type") Optional<String> type) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "OPTIONAL_PATH_VARIABLE");
        response.put("extractedValue", type.orElse("DEFAULT_INFO"));
        return response;
    }

    // 3. Multiple Path Variables
    // URL: /api/scenario86/catalogs/electronics/items/45
    @GetMapping("/catalogs/{cat}/items/{itemId}")
    public Map<String, Object> getCatalogItem(@PathVariable String cat, @PathVariable String itemId) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "MULTIPLE_PATH_VARIABLES");
        response.put("category", cat);
        response.put("itemId", itemId);
        return response;
    }

    // 4. Regex Validation in Path Variable
    // URL: /api/scenario86/codes/123 (Passes)
    // URL: /api/scenario86/codes/abc (Fails with 404)
    @GetMapping("/codes/{code:[0-9]{3}}")
    public Map<String, Object> validateCode(@PathVariable String code) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "REGEX_VALIDATED_PATH_VARIABLE");
        response.put("code", code);
        return response;
    }

    // 5. Query Parameter (Required vs Optional with Default)
    // URL: /api/scenario86/search?query=java&page=2
    @GetMapping("/search")
    public Map<String, Object> search(
            @RequestParam String query, // Required
            @RequestParam(defaultValue = "0") int page, // Optional with default
            @RequestParam(required = false) String sort // Optional (returns null if missing)
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "MIXED_QUERY_PARAMS");
        response.put("query", query);
        response.put("page", page);
        response.put("sort", sort != null ? sort : "none");
        return response;
    }

    // 6. Handling Multiple Values for one parameter
    // URL: /api/scenario86/tags?values=java,spring,docker
    @GetMapping("/tags")
    public Map<String, Object> getTags(@RequestParam List<String> values) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "MULTI_VALUE_QUERY_PARAM");
        response.put("tagsCount", values.size());
        response.put("tags", values);
        return response;
    }

    // 7. Dynamic Parameters into a Map
    // URL: /api/scenario86/dynamic?key1=val1&key2=val2&anything=goes
    @GetMapping("/dynamic")
    public Map<String, Object> collectAll(@RequestParam Map<String, String> allParams) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "DYNAMIC_MAP_QUERY_PARAMS");
        response.put("allParams", allParams);
        return response;
    }
}
