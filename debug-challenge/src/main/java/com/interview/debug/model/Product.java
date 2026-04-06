package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@NamedQuery(
    name = "Product.findActiveProducts",
    query = "SELECT p FROM Product p WHERE p.active = true"
)
@NamedNativeQuery(
    name = "Product.findExpensiveProductsNative",
    query = "SELECT * FROM product WHERE price > :minPrice",
    resultClass = Product.class
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Double price;
    private Boolean active;
}
