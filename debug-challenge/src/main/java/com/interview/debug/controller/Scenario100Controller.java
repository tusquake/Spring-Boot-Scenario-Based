package com.interview.debug.controller;

import com.interview.debug.model.Scenario100InventoryItem;
import com.interview.debug.model.Scenario100Metadata;
import com.interview.debug.repository.Scenario100InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Scenario 100: JSON Columns in JPA.
 * Demonstrates CRUD on entities with native JSON column mapping.
 */
@RestController
@RequestMapping("/api/scenario100")
@RequiredArgsConstructor
public class Scenario100Controller {

    private final Scenario100InventoryRepository inventoryRepository;

    @PostMapping("/create")
    public ResponseEntity<Scenario100InventoryItem> createItem(
            @RequestParam String name,
            @RequestParam Double weight) {

        Scenario100Metadata metadata = Scenario100Metadata.builder()
                .origin("GLOBAL-WAREHOUSE")
                .weight(weight)
                .tags(List.of("NEW", "PREMIUM", "SCENARIO-100"))
                .attributes(Map.of("Material", "Titanium", "Warranty", "10Y"))
                .build();

        Scenario100InventoryItem item = Scenario100InventoryItem.builder()
                .name(name)
                .metadata(metadata)
                .build();

        return ResponseEntity.ok(inventoryRepository.save(item));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Scenario100InventoryItem> findItem(@PathVariable Long id) {
        return inventoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Scenario100InventoryItem>> findAll() {
        return ResponseEntity.ok(inventoryRepository.findAll());
    }
}
