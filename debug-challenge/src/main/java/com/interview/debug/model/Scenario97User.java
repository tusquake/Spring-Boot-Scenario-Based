package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Scenario 97: Entity with Multiple Embedded Addresses.
 * This class demonstrates @Embedded and the necessity of @AttributeOverrides
 * when including two instances of the same Embeddable class.
 */
@Entity
@Table(name = "scenario97_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario97User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * Default embedding: Will use field names from Scenario97Address as column names.
     */
    @Embedded
    private Scenario97Address homeAddress;

    /**
     * Overridden embedding: We must override column names because 'street',
     * 'city', and 'zipCode' already exist from the homeAddress.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "work_street")),
        @AttributeOverride(name = "city", column = @Column(name = "work_city")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "work_zip"))
    })
    private Scenario97Address workAddress;
}
