package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Scenario 98: JPA Inheritance - Single Table Strategy.
 * This is the abstract base class for our Vehicle hierarchy.
 * In SINGLE_TABLE strategy, all subclasses (Car, Bike) are stored in ONE table.
 */
@Entity
@Table(name = "scenario98_vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Scenario98Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufacturer;
}
