package com.interview.debug.controller;

import com.interview.debug.model.Scenario81Product;
import com.interview.debug.repository.Scenario81ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenario81")
public class Scenario81Controller {

    private final Scenario81ProductRepository productRepository;

    public Scenario81Controller(Scenario81ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/products")
    public Scenario81Product createProduct(@RequestBody Scenario81Product product) {
        // Notice we don't manually set createdBy or createdDate
        return productRepository.save(product);
    }

    @PutMapping("/products/{id}")
    public Scenario81Product updateProduct(@PathVariable Long id, @RequestBody Scenario81Product productDetails) {
        Scenario81Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        
        // Notice we don't manually set lastModifiedBy or lastModifiedDate
        return productRepository.save(product);
    }

    @GetMapping("/products")
    public List<Scenario81Product> getAllProducts() {
        return productRepository.findAll();
    }
}
