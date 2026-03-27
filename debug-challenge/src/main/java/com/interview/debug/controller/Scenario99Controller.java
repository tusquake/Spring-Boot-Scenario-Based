package com.interview.debug.controller;

import com.interview.debug.model.Scenario99OrderItem;
import com.interview.debug.model.Scenario99OrderItemId;
import com.interview.debug.repository.Scenario99OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Scenario 99: JPA Composite Keys.
 */
@RestController
@RequestMapping("/api/scenario99")
@RequiredArgsConstructor
public class Scenario99Controller {

    private final Scenario99OrderItemRepository repository;

    /**
     * Saving with a Composite Key:
     * We must instantiate the 'Scenario99OrderItemId' object.
     */
    @PostMapping("/create")
    public Scenario99OrderItem createItem(@RequestParam Long orderId, 
                                          @RequestParam Long productId,
                                          @RequestParam int quantity) {
        Scenario99OrderItemId id = new Scenario99OrderItemId(orderId, productId);
        
        Scenario99OrderItem item = Scenario99OrderItem.builder()
                .id(id)
                .quantity(quantity)
                .price(99.99)
                .note("Created with Composite Key!")
                .build();

        return repository.save(item);
    }

    /**
     * Finding by a Composite Key:
     * We pass the ID object to findById().
     */
    @GetMapping("/find")
    public Optional<Scenario99OrderItem> findItem(@RequestParam Long orderId, 
                                                @RequestParam Long productId) {
        Scenario99OrderItemId id = new Scenario99OrderItemId(orderId, productId);
        return repository.findById(id);
    }
}
