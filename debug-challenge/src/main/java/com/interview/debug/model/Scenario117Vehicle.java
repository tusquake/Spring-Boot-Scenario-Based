package com.interview.debug.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Scenario 117: Abstract Base Class
 * Use @JsonTypeInfo to tell Jackson HOW to include the type info.
 * Use @JsonSubTypes to tell Jackson WHICH subclasses exist.
 */
@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Scenario117Car.class, name = "car"),
    @JsonSubTypes.Type(value = Scenario117Bike.class, name = "bike"),
    @JsonSubTypes.Type(value = Scenario117Truck.class, name = "truck")
})
public abstract class Scenario117Vehicle implements Serializable {
    private String brand;
    private String model;
    private double price;

    /**
     * Polymorphic method to get specific details for each vehicle type.
     */
    public abstract String getDetails();
}
