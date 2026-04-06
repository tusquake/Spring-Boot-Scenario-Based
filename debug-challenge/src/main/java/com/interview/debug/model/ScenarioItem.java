package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@NamedQuery(
    name = "ScenarioItem.findActiveItems",
    query = "SELECT i FROM ScenarioItem i WHERE i.active = true"
)
@NamedNativeQuery(
    name = "ScenarioItem.findExpensiveItemsNative",
    query = "SELECT * FROM scenario_item WHERE price > :minPrice",
    resultClass = ScenarioItem.class
)
public class ScenarioItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Double price;
    private Boolean active;
}
