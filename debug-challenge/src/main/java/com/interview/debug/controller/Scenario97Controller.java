package com.interview.debug.controller;

import com.interview.debug.model.Scenario97Address;
import com.interview.debug.model.Scenario97Company;
import com.interview.debug.model.Scenario97User;
import com.interview.debug.model.Scenario97Warehouse;
import com.interview.debug.repository.Scenario97CompanyRepository;
import com.interview.debug.repository.Scenario97UserRepository;
import com.interview.debug.repository.Scenario97WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for Scenario 97: JPA Embeddables.
 */
@RestController
@RequestMapping("/api/scenario97")
@RequiredArgsConstructor
public class Scenario97Controller {

    private final Scenario97UserRepository userRepository;
    private final Scenario97CompanyRepository companyRepository;
    private final Scenario97WarehouseRepository warehouseRepository;

    /**
     * Seed behavior: Creating a user with both Home and Work addresses.
     */
    @PostMapping("/seed")
    public Scenario97User seedUser() {
        Scenario97Address home = Scenario97Address.builder()
                .street("123 Maple St")
                .city("Springfield")
                .zipCode("11001")
                .build();

        Scenario97Address work = Scenario97Address.builder()
                .street("456 Industrial Way")
                .city("Metropolis")
                .zipCode("22002")
                .build();

        Scenario97User user = Scenario97User.builder()
                .name("John ValueObject")
                .homeAddress(home)
                .workAddress(work)
                .build();

        return userRepository.save(user);
    }

    @GetMapping("/users")
    public List<Scenario97User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Demonstrating null handling:
     * If an Embedded object is entirely null, JPA preserves it as null.
     */
    @PostMapping("/create-no-work")
    public Scenario97User createUserNoWork(@RequestParam String name) {
        Scenario97Address home = Scenario97Address.builder()
                .street("789 Silent Way")
                .city("Ghost Town")
                .zipCode("00000")
                .build();

        Scenario97User user = Scenario97User.builder()
                .name(name)
                .homeAddress(home)
                .workAddress(null) // Should result in all work_* columns being NULL
                .build();

        return userRepository.save(user);
    }

    /**
     * Demonstrating Reusability:
     * One Address class used in 3 different Entities/Tables.
     */
    @PostMapping("/global-seed")
    public Map<String, Object> globalSeed() {
        Scenario97Address commonAddr = Scenario97Address.builder()
                .street("1 Global Blvd")
                .city("Tech City")
                .zipCode("99999")
                .build();

        // 1. User
        Scenario97User user = Scenario97User.builder()
                .name("Global Architect")
                .homeAddress(commonAddr)
                .build();
        userRepository.save(user);

        // 2. Company
        Scenario97Company company = Scenario97Company.builder()
                .companyName("The Reuse Corp")
                .headQuarterAddress(commonAddr)
                .build();
        companyRepository.save(company);

        // 3. Warehouse
        Scenario97Warehouse warehouse = Scenario97Warehouse.builder()
                .warehouseCode("WH-001")
                .location(commonAddr)
                .build();
        warehouseRepository.save(warehouse);

        return Map.of(
            "user", user,
            "company", company,
            "warehouse", warehouse,
            "message", "Successfully created 3 different entities using the SAME Address class!"
        );
    }
}
