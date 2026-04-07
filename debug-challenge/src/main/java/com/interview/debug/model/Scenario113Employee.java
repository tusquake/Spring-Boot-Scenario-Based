package com.interview.debug.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Scenario 113: Employee Entity
 * This entity will be automatically exported via Spring Data REST.
 * We include a sensitive 'salary' field to demonstrate Projections later.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scenario113Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String position;
    private Double salary; // Sensitive field
}
