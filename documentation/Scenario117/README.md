# Scenario 117: Jackson Polymorphism & Open-Closed Principle 🚛

## Overview
How do you build an API that is **"Open for Extension, but Closed for Modification"**? 

In this scenario, we demonstrate how **Jackson Polymorphism** combined with **Java Polymorphism** allows you to add entirely new types (like `Truck`) to your system without changing a single line of logic in your Controllers.

---

## 🚀 Key Patterns

### 1. The "Type Discriminator" (@JsonTypeInfo)
Jackson uses a field (in our case, `type`) to determine which subclass to create during deserialization.
- `use = Id.NAME`: Uses logical aliases (`car`, `bike`, `truck`) instead of internal class names.

### 2. Polymorphic Details (The Evolution)
Instead of using `instanceof` checks in the Controller, we moved the logic into an abstract method `getDetails()` in the base class.
- **Car**: Implement `getDetails()` for car-specific fields.
- **Bike**: Implement `getDetails()` for bike-specific fields.
- **Truck**: Implement `getDetails()` for truck-specific fields.

### 3. "Zero-Change" Controller
The `Scenario117Controller` now iterates over a `List<Vehicle>` and calls `v.getDetails()`. 
- **The Magic**: When we added the `Truck` class, we ONLY registered it in the base `Vehicle` class. The Controller automatically picked it up and processed it without being modified.

---

## 🧪 How to Test

### 1. The Bulk Mixed Request
Send a list containing a Car, a Bike, and a Truck in **ONE request**:

```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario117/fleet" \
     -H "Content-Type: application/json" \
     -d '[
           {"type": "car", "brand": "Tesla", "price": 90000, "seatingCapacity": 5},
           {"type": "bike", "brand": "Harley", "price": 20000, "hasSidecar": false},
           {"type": "truck", "brand": "Volvo", "price": 150000, "payloadCapacity": 18.5}
         ]'
```

### 2. Verify Output
The response will show the total fleet value and the unique details for each vehicle, even though they have different fields.

---

## Interview Tip 💡

**Q**: *"How do you handle inheritance in REST APIs with Spring Boot?"*  
**A**: *"We use Jackson's @JsonTypeInfo and @JsonSubTypes. This allows us to define a 'Type Discriminator' field in the JSON. For clean architecture, we also follow the Open-Closed Principle by using polymorphic methods in the base class, so our Controllers don't need to change when we add new subclasses."*

**Q**: *"What is the benefit of using logical names over class names in @JsonTypeInfo?"*  
**A**: *"Using logical names (like 'car') decouples the API contract from the internal Java package structure. This prevents 'Insecure Deserialization' vulnerabilities and allows you to rename or move classes without breaking existing clients."*
