package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Demonstrates GenerationType.IDENTITY.
 * DB handles increment (e.g., AUTO_INCREMENT in MySQL/H2).
 * Disables JDBC batching because Hibernate needs to perform an INSERT 
 * immediately to get the ID.
 */
@Entity
@Table(name = "scenario94_identity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario94Identity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
