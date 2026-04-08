package com.interview.debug.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Scenario 117: Bike Subclass
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Scenario117Bike extends Scenario117Vehicle {
    private boolean hasSidecar;
    private String handleBarType;

    @Override
    public String getDetails() {
        return String.format("Bike: %s %s ($%.2f) - Sidecar: %s", 
            getBrand(), getModel(), getPrice(), hasSidecar);
    }
}
