package com.interview.debug.controller;

import com.interview.debug.model.Scenario98Bike;
import com.interview.debug.model.Scenario98Car;
import com.interview.debug.model.Scenario98Vehicle;
import com.interview.debug.repository.Scenario98VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Scenario 98: JPA Inheritance - Single Table Strategy.
 */
@RestController
@RequestMapping("/api/scenario98")
@RequiredArgsConstructor
public class Scenario98Controller {

    private final Scenario98VehicleRepository vehicleRepository;

    /**
     * Polymorphic Save:
     * Even though we use 'vehicleRepository', Hibernate handles the 
     * correct DiscriminatorValue ('CAR' or 'BIKE').
     */
    @PostMapping("/seed")
    public String seedInventory() {
        // 1. Save a Car
        Scenario98Car car = Scenario98Car.builder()
                .manufacturer("Tesla")
                .numberOfDoors(4)
                .engineType("Electric")
                .build();
        vehicleRepository.save(car);

        // 2. Save a Bike
        Scenario98Bike bike = Scenario98Bike.builder()
                .manufacturer("Ducati")
                .hasCarrier(false)
                .handleType("Sport")
                .build();
        vehicleRepository.save(bike);

        return "Successfully seeded 1 Car and 1 Bike into a SINGLE table!";
    }

    /**
     * Polymorphic Query:
     * Fetching from the base class repository returns ALL subclasses 
     * with their specific fields materialized.
     */
    @GetMapping("/vehicles")
    public List<Scenario98Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}
