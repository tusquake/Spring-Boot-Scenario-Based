package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Audit entity to track transaction outcomes in Scenario 93.
 */
@Entity
@Table(name = "scenario93_audits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario93Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private String status;

    private String detail;
}
