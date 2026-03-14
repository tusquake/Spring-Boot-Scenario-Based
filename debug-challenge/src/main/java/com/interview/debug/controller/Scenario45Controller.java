package com.interview.debug.controller;

import com.interview.debug.model.Customer;
import com.interview.debug.repository.CustomerRepository;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityManager;
import java.util.List;

@RestController
@RequestMapping("/api/scenario45")
public class Scenario45Controller {

    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;

    public Scenario45Controller(CustomerRepository customerRepository, EntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.entityManager = entityManager;
    }

    /**
     * Step 1: Create or Update a user to trigger a new revision
     */
    @GetMapping("/update/{id}/{newName}")
    @Transactional
    public String updateCustomer(@PathVariable Long id, @PathVariable String newName) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        String oldName = customer.getName();
        customer.setName(newName);
        customerRepository.save(customer);
        
        return String.format("Updated Customer %d: '%s' -> '%s'. New revision created in CUSTOMERS_AUD!", 
                id, oldName, newName);
    }

    /**
     * Step 2: Retrieve the history of a customer using AuditReader
     * Refactored to return DTOs to avoid infinite recursion/proxy issues.
     */
    @GetMapping("/history/{id}")
    public List<AuditLogDto> getHistory(@PathVariable Long id) {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = (List<Object[]>) reader.createQuery()
                .forRevisionsOfEntity(Customer.class, false, true)
                .add(org.hibernate.envers.query.AuditEntity.id().eq(id))
                .getResultList();

        return results.stream().map(row -> {
            Customer entity = (Customer) row[0];
            org.hibernate.envers.DefaultRevisionEntity revEntity = (org.hibernate.envers.DefaultRevisionEntity) row[1];
            org.hibernate.envers.RevisionType revType = (org.hibernate.envers.RevisionType) row[2];
            
            return new AuditLogDto(
                revEntity.getId(),
                revEntity.getTimestamp(),
                revType.name(),
                entity.getName(),
                entity.getEmail()
            );
        }).toList();
    }

    public static record AuditLogDto(
        int revisionId,
        long timestamp,
        String operation,
        String name,
        String email
    ) {}
}
