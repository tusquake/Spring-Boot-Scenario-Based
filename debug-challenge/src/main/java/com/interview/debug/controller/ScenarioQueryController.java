package com.interview.debug.controller;

import com.interview.debug.repository.ScenarioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario/query")
@RequiredArgsConstructor
public class ScenarioQueryController {

    private final ScenarioItemRepository itemRepository;

    @GetMapping("/demo")
    public Map<String, Object> runQueryDemonstration() {
        Map<String, Object> result = new HashMap<>();

        result.put("1_query_jpql", itemRepository.searchByNameQuery("ouse"));
        result.put("2_query_native", itemRepository.findMostExpensiveActiveItemNative());
        result.put("3_named_query", itemRepository.findActiveItems());
        result.put("4_named_native_query", itemRepository.findExpensiveItemsNative(90.0));

        result.put("explanation", Map.of(
            "@Query", "Written directly in the Repository interface. Uses JPQL by default. Easiest to read alongside the method signature.",
            "@Query(native=true)", "Uses plain SQL. Good for database-specific functions (like JSONB logic or hints).",
            "@NamedQuery", "Written on the @Entity class using JPQL. Checked for syntax errors when Spring Boot starts up. Reusable across multiple repositories.",
            "@NamedNativeQuery", "Written on the @Entity class using native SQL. Ties your application to a specific database engine (like Postgres/MySQL)."
        ));

        return result;
    }
}
