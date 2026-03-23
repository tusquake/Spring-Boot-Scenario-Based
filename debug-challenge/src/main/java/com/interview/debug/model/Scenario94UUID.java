package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

/**
 * Demonstrates GenerationType.UUID (Modern Hibernate Strategy).
 * IDs are generated in the application layer, not the DB.
 * Scaling: Best for distributed systems (No DB round-trip for IDs).
 * Performance: Slightly overhead for size (16 bytes) but saves DB calls.
 */
@Entity
@Table(name = "scenario94_uuid")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario94UUID {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
}
