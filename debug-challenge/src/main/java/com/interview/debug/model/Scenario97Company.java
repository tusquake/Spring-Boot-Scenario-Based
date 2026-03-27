package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Scenario 97: Company Entity.
 * Reuses the same Scenario97Address class.
 */
@Entity
@Table(name = "scenario97_companies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario97Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    @Embedded
    private Scenario97Address headQuarterAddress;
}
