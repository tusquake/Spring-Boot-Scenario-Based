package com.interview.debug.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Scenario 97: JPA Embeddable Address.
 * This class is NOT an entity. It does not have its own table.
 * It is a "Value Object" that will be stored in the parent's table.
 */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario97Address {
    private String street;
    private String city;
    private String zipCode;
}
