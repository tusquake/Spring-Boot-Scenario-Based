package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity to demonstrate multi-tenant data isolation.
 */
@Entity
@Table(name = "scenario91_projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario91Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;
}
