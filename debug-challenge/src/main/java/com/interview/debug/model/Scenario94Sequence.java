package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Demonstrates GenerationType.SEQUENCE.
 * JPA fetches values from a database sequence.
 * Allows JDBC batching because Hibernate can pre-fetch IDs without performing an INSERT.
 */
@Entity
@Table(name = "scenario94_sequence")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario94Sequence {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sc94_seq")
    @SequenceGenerator(name = "sc94_seq", sequenceName = "scenario94_seq", allocationSize = 50)
    private Long id;

    private String name;
}
