package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Demonstrates GenerationType.TABLE.
 * Uses a dedicated table to manage IDs. 
 * Most portable but slowest due to separate table locks/updates.
 */
@Entity
@Table(name = "scenario94_table_demo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario94TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sc94_table_gen")
    @TableGenerator(name = "sc94_table_gen", table = "id_gen_table", 
                    pkColumnName = "gen_name", valueColumnName = "gen_value",
                    pkColumnValue = "scenario94_id", allocationSize = 10)
    private Long id;

    private String name;
}
