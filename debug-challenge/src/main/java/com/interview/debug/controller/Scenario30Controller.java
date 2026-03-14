package com.interview.debug.controller;

import com.interview.debug.model.Customer;
import com.interview.debug.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ScrollPosition.Direction;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario30")
public class Scenario30Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario30Controller.class);
    private final CustomerRepository customerRepository;
    private final com.interview.debug.repository.OrderRepository orderRepository;

    public Scenario30Controller(CustomerRepository customerRepository,
            com.interview.debug.repository.OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Seed 10,000 records so we can see the impact of pagination queries.
     */
    @PostConstruct
    public void seedData() {
        if (customerRepository.count() == 0) {
            logger.info("Seeding 10,000 customers for Pagination Demo...");
            for (int i = 1; i <= 100; i++) {
                Customer c = new Customer();
                c.setName("Customer " + i);
                c.setEmail("customer" + i + "@example.com");
                customerRepository.save(c);

                // Add 2 orders for each customer
                com.interview.debug.model.Order o1 = new com.interview.debug.model.Order();
                o1.setProductName("Product A for " + i);
                o1.setCustomer(c);
                o1.setPrice(100.0);
                o1.setOrderDate(new java.util.Date());

                com.interview.debug.model.Order o2 = new com.interview.debug.model.Order();
                o2.setProductName("Product B for " + i);
                o2.setCustomer(c);
                o2.setPrice(200.0);
                o2.setOrderDate(new java.util.Date());

                orderRepository.save(o1);
                orderRepository.save(o2);

                if (i % 1000 == 0) {
                    logger.info("Seeded {} customers with orders...", i);
                }
            }
            logger.info("Seeding complete.");
        }
    }

    /**
     * 1. Page (Classic offset pagination)
     * Pros: Easy, UI gets total pages.
     * Cons: Issues a `SELECT COUNT(*)` query. Deep pagination is slow due to
     * OFFSET.
     */
    @GetMapping("/page")
    public Page<Customer> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Executing PAGE query for page {}, size {}", page, size);
        return customerRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    /**
     * 2. Slice (Infinite Scroll / Load More)
     * Pros: No COUNT(*) query. It fetches size+1 to know if there's a next page.
     * Cons: Still uses OFFSET under the hood, so deep pagination is still slow.
     */
    @GetMapping("/slice")
    public Slice<Customer> getSlice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Executing SLICE query for page {}, size {}", page, size);
        return customerRepository.findBy(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    /**
     * 3. Cursor / Keyset Pagination (Spring Boot 3 Window)
     * Pros: NO OFFSET, NO COUNT. Uses `WHERE id > ?`. O(1) performance even for
     * page 1,000,000.
     * Cons: Cannot jump to a specific page (e.g., jump to page 5).
     * 
     * How to use:
     * First request: /cursor
     * Next request: /cursor?cursor={position_opaque_string_from_previous_response}
     */
    @GetMapping("/cursor")
    public Map<String, Object> getCursor(@RequestParam(required = false) String cursor) {

        ScrollPosition position = (cursor != null && !cursor.trim().isEmpty())
                ? ScrollPosition.of(Map.of("id", Long.parseLong(cursor)), Direction.FORWARD)
                : ScrollPosition.keyset();

        logger.info("Executing CURSOR query with position: {}", position);
        Window<Customer> window = customerRepository.findFirst10ByOrderByIdAsc(position);

        // Extract the next cursor value manually for simpler client usage
        String nextCursor = null;
        if (!window.isEmpty() && window.hasNext()) {
            ScrollPosition rawPosition = window.positionAt(window.size() - 1);
            if (rawPosition instanceof org.springframework.data.domain.KeysetScrollPosition keysetPosition) {
                Map<String, Object> keys = keysetPosition.getKeys();
                if (keys.containsKey("id")) {
                    nextCursor = keys.get("id").toString();
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", window.getContent());
        response.put("hasNext", window.hasNext());
        response.put("nextCursor", nextCursor);
        return response;
    }
}
