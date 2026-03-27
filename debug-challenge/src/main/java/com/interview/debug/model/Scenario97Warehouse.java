package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Scenario 97: Warehouse Entity.
 * Reuses the same Scenario97Address class once more.
 */
@Entity
@Table(name = "scenario97_warehouses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario97Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String warehouseCode;

    @Embedded
    private Scenario97Address location;
}
