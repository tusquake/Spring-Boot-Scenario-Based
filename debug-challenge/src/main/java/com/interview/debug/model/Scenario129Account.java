package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Scenario 129: Transaction Isolation Levels
 * Entity representing a bank account for concurrency testing.
 */
@Entity
@Table(name = "scenario129_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scenario129Account implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String owner;
    
    private Double balance;
}
