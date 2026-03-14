package com.interview.debug.controller;

import com.interview.debug.model.Customer;
import com.interview.debug.repository.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario42")
public class Scenario42Controller {

    private final CustomerRepository customerRepository;

    public Scenario42Controller(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/create")
    public Map<String, Object> createAndShow() {
        Customer c = new Customer();
        c.setName("Audit Test User");
        c.setEmail("audit@test.com");
        
        Customer saved = customerRepository.save(c);
        
        System.out.println("Customer saved. ID: " + saved.getId());
        System.out.println("Created Date: " + saved.getCreatedDate());
        
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("id", saved.getId());
        response.put("name", saved.getName());
        response.put("createdDate", saved.getCreatedDate());
        response.put("lastModifiedDate", saved.getLastModifiedDate());
        
        return response;
    }
}
