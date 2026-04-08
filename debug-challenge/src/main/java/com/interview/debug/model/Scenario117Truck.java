package com.interview.debug.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Scenario 117: Truck Subclass (The "New Type")
 * Added WITHOUT changing the existing controller logic.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Scenario117Truck extends Scenario117Vehicle {
    private double payloadCapacity;
    private int numberOfAxles;

    @Override
    public String getDetails() {
        return String.format("Truck: %s %s ($%.2f) - Payload: %.1f Tons", 
            getBrand(), getModel(), getPrice(), payloadCapacity);
    }
}
