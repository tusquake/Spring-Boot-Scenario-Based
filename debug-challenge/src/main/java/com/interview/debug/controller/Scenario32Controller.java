package com.interview.debug.controller;

import com.interview.debug.model.Customer;
import com.interview.debug.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scenario32")
public class Scenario32Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario32Controller.class);
    private final CustomerRepository customerRepository;

    public Scenario32Controller(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Demonstrate N+1 Problem (The "Bad Waiter").
     * One query is executed for Customers.
     * Then, as we iterate and access .getOrders(), N more queries are executed.
     */
    @GetMapping("/nplus1")
    public List<Map<String, Object>> getNPlus1() {
        logger.info("Executing N+1 demonstration...");
        List<Customer> customers = customerRepository.findAll().stream().limit(10).toList();
        
        return customers.stream().map(c -> {
            logger.info("Accessing orders for customer: {}", c.getName());
            // This call triggers a SELECT for each customer if not already loaded
            int orderCount = c.getOrders().size(); 
            
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("customer", c.getName());
            map.put("orderCount", orderCount);
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * Demonstrate Optimized Fetching with Entity Graph (The "Big Tray").
     * One single JOIN query is executed for both Customers and Orders.
     */
    @GetMapping("/optimized")
    public List<Map<String, Object>> getOptimized() {
        logger.info("Executing optimized fetching using Entity Graph...");
        List<Customer> customers = customerRepository.findAllWithOrders().stream().limit(10).toList();

        return customers.stream().map(c -> {
            // Orders are already in memory from the JOIN fetch
            int orderCount = c.getOrders().size(); 
            
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("customer", c.getName());
            map.put("orderCount", orderCount);
            return map;
        }).collect(Collectors.toList());
    }
}
