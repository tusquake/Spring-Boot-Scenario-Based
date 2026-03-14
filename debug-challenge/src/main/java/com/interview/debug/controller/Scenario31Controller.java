package com.interview.debug.controller;

import com.interview.debug.model.Customer;
import com.interview.debug.repository.CustomerRepository;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/scenario31")
public class Scenario31Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario31Controller.class);
    private final CustomerRepository customerRepository;

    public Scenario31Controller(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * The "Tinder Filter" Search API.
     * All parameters are optional. The query builds itself based on what's provided.
     */
    @GetMapping("/search")
    public List<Customer> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long minId,
            @RequestParam(required = false) Long maxId) {

        logger.info("Dynamic Search Request -> name: {}, email: {}, minId: {}, maxId: {}", name, email, minId, maxId);

        Specification<Customer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Add "Name Like" filter if name is provided
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // 2. Add "Email Like" filter if email is provided
            if (email != null && !email.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            // 3. Add "Greater than ID" if minId provided
            if (minId != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("id"), minId));
            }

            // 4. Add "Less than ID" if maxId provided
            if (maxId != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("id"), maxId));
            }

            // Combine all predicates with AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return customerRepository.findAll(spec);
    }
}
