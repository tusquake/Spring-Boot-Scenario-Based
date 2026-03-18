package com.interview.debug.controller;

import com.interview.debug.model.Customer;
import com.interview.debug.repository.CustomerRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario65")
public class Scenario65Controller {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRepository customerRepository;

    public Scenario65Controller(JdbcTemplate jdbcTemplate, CustomerRepository customerRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRepository = customerRepository;
    }

    /**
     * VULNERABLE: Uses raw string concatenation.
     * Attack: /api/scenario65/vulnerable/search?name=' OR '1'='1
     */
    @GetMapping("/vulnerable/search")
    public List<Map<String, Object>> vulnerableSearch(@RequestParam String name) {
        String sql = "SELECT id, name, email FROM customers WHERE name = '" + name + "'";
        System.out.println("Executing VULNERABLE SQL: " + sql);
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * SECURE (JDBC): Uses parameterized query (Prepared Statement).
     * Input is treated as data, not code.
     */
    @GetMapping("/secure/jdbc/search")
    public List<Map<String, Object>> secureJdbcSearch(@RequestParam String name) {
        String sql = "SELECT id, name, email FROM customers WHERE name = ?";
        System.out.println("Executing SECURE JDBC SQL: " + sql);
        return jdbcTemplate.queryForList(sql, name);
    }

    /**
     * SECURE (JPA): Uses Spring Data JPA Repository.
     * JPA uses prepared statements under the hood.
     */
    @GetMapping("/secure/jpa/search")
    public List<Customer> secureJpaSearch(@RequestParam String name) {
        System.out.println("Executing SECURE JPA Search for name: " + name);
        // This is safe because JPA uses Parameterized Queries
        return customerRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .toList();
        
        // Note: In a real app, you'd use customerRepository.findByName(name)
        // which is also perfectly safe.
    }
}
