# Scenario 98: JPA Inheritance - SINGLE_TABLE Strategy

## Overview
How do you map an object-oriented class hierarchy (Inheritance) to a relational database table that doesn't support subclasses?

JPA provides three main strategies, and **SINGLE_TABLE** is the most common for performance.

---

## 🏗️ The Strategy: `SINGLE_TABLE`
In this strategy, **all classes** in the hierarchy are mapped to a **single database table**. 

### 1. The Mapper (Discriminator)
A special column (e.g., `vehicle_type`) is added to the table. Hibernate uses this to know whether a row should be instantiated as a `Car` or a `Bike`.

### 2. The Table Structure
The table `scenario98_vehicles` will look like this:

| id | manufacturer | vehicle_type | number_of_doors | has_carrier | ... |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | Tesla | **CAR** | 4 | NULL | ... |
| 2 | Ducati | **BIKE** | NULL | false | ... |

---

## 🚀 Key Advantages
- **High Performance**: No SQL `JOIN` or `UNION` is needed. Fetching all vehicles is a simple `SELECT *`.
- **Simplistic**: Only one table to manage in your schema migration scripts.

## ⚠️ Key Disadvantages
- **Data Integrity**: Columns specific to subclasses (like `numberOfDoors`) **must be nullable**, because they will be null for every other subclass (like `Bike`).
- **Table Bloat**: If you have many subclasses with many unique fields, the table becomes very "wide" and sparse (lots of NULLs).

---

## 🧪 Testing

### 1. Seed the Inventory (Polymorphic Save)
This endpoint saves one `Car` and one `Bike` using a single `VehicleRepository`.
```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario98/seed"
```

### 2. Fetch All Vehicles (Polymorphic Query)
This shows the power of inheritance. A single call to `findAll()` returns a JSON list containing different object types.
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario98/vehicles"
```

---

## Interview Tip 💡
**Q**: *"What are the other two inheritance strategies and when should you use them?"*  
**A**: 
1. **JOINED**: Each class has its own table. Best for data integrity (no nulls) but requires heavy joins.
2. **TABLE_PER_CLASS**: Each concrete class has its own table. No joins needed for specific types, but a "Find All" query requires expensive `UNION` operations across all tables.
