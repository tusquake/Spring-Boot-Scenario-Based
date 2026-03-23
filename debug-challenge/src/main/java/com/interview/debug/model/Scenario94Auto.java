package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Demonstrates GenerationType.AUTO.
 * Hibernate chooses the best strategy (usually SEQUENCE for H2/Postgres, 
 * or TABLE if no sequences exist).
 */
@Entity
@Table(name = "scenario94_auto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario94Auto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
