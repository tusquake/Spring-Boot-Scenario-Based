package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Scenario 100: JSON Columns in JPA (Hibernate 6).
 */
@Entity
@Table(name = "scenario100_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Scenario100InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    // Hibernate 6 handles JSON serialization/deserialization automatically!
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "item_metadata", columnDefinition = "json")
    Scenario100Metadata metadata;
}
