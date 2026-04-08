package com.interview.debug.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Scenario 117: Car Subclass
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Scenario117Car extends Scenario117Vehicle {
    private int seatingCapacity;
    private boolean hasSunroof;

    @Override
    public String getDetails() {
        return String.format("Car: %s %s ($%.2f) - %d seats", 
            getBrand(), getModel(), getPrice(), seatingCapacity);
    }
}
