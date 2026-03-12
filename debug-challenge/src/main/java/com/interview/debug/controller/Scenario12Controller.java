package com.interview.debug.controller;

import com.interview.debug.model.SoftDeleteProduct;
import com.interview.debug.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenario12")
public class Scenario12Controller {

    private final ProductRepository productRepository;

    public Scenario12Controller(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/init")
    public String init() {
        SoftDeleteProduct p1 = new SoftDeleteProduct();
        p1.setName("iPhone 15");
        p1.setPrice(999.0);
        productRepository.save(p1);

        SoftDeleteProduct p2 = new SoftDeleteProduct();
        p2.setName("MacBook M3");
        p2.setPrice(1999.0);
        productRepository.save(p2);

        return "Initialized: 2 Products created.";
    }

    @GetMapping("/all")
    public List<SoftDeleteProduct> getAll() {
        // This will only return products where deleted = false
        return productRepository.findAll();
    }

    @GetMapping("/all-including-deleted")
    public List<SoftDeleteProduct> getAllIncludingDeleted() {
        // This uses a native query to skip the @SQLRestriction
        return productRepository.findAllIncludingDeleted();
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        // This will trigger the @SQLDelete update instead of a real delete
        productRepository.deleteById(id);
        return "Product with ID " + id + " soft-deleted successfully!";
    }
}
