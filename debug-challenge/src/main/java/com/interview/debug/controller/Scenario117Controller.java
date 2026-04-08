package com.interview.debug.controller;

import com.interview.debug.model.Scenario117Bike;
import com.interview.debug.model.Scenario117Car;
import com.interview.debug.model.Scenario117Vehicle;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Scenario 117: Polymorphic API Controller
 */
@RestController
@RequestMapping("/api/scenario117")
public class Scenario117Controller {

    /**
     * Demonstrates Polymorphic Deserialization and Bulk Operations.
     * Jackson will look at the 'type' field in each item of the list
     * and decide whether to create a Car or a Bike.
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/fleet")
    public Map<String, Object> processFleet(@RequestBody List<Scenario117Vehicle> fleet) {
        int carCount = 0;
        int bikeCount = 0;
        double totalPrice = 0;
        List<String> details = new ArrayList<>();

        for (Scenario117Vehicle v : fleet) {
            totalPrice += v.getPrice();
            details.add(v.getDetails());
            
            // Still counting types for the summary report
            if (v instanceof Scenario117Car) carCount++;
            else if (v instanceof Scenario117Bike) bikeCount++;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalVehicles", fleet.size());
        response.put("totalFleetValue", "$" + totalPrice);
        response.put("carCount", carCount);
        response.put("bikeCount", bikeCount);
        response.put("processedDetails", details);
        return response;
    }

    /**
     * Demonstrates Polymorphic Serialization.
     * Jackson will automatically add the 'type' field to the outgoing JSON.
     */
    @GetMapping("/mixed")
    public List<Scenario117Vehicle> getMixedFleet() {
        List<Scenario117Vehicle> fleet = new ArrayList<>();

        Scenario117Car car = new Scenario117Car();
        car.setBrand("Tesla");
        car.setModel("Model S");
        car.setPrice(90000);
        car.setSeatingCapacity(5);
        car.setHasSunroof(true);
        fleet.add(car);

        Scenario117Bike bike = new Scenario117Bike();
        bike.setBrand("Harley");
        bike.setModel("Fat Boy");
        bike.setPrice(20000);
        bike.setHasSidecar(false);
        bike.setHandleBarType("Ape Hangers");
        fleet.add(bike);

        return fleet;
    }
}
