package com.interview.debug.controller;

import com.interview.debug.model.Gadget;
import com.interview.debug.repository.GadgetRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/scenario30b")
public class Scenario30BController {

    private static final Logger logger = LoggerFactory.getLogger(Scenario30BController.class);
    private final GadgetRepository gadgetRepository;

    public Scenario30BController(GadgetRepository gadgetRepository) {
        this.gadgetRepository = gadgetRepository;
    }

    @PostConstruct
    public void seedGadgets() {
        if (gadgetRepository.count() == 0) {
            logger.info("Seeding gadgets for Scenario 30B...");
            
            Gadget g1 = new Gadget(); g1.setName("iPhone 15"); g1.setCategory("Smartphone"); g1.setPrice(999.99);
            Gadget g2 = new Gadget(); g2.setName("Samsung S24"); g2.setCategory("Smartphone"); g2.setPrice(899.99);
            Gadget g3 = new Gadget(); g3.setName("MacBook Pro"); g3.setCategory("Laptop"); g3.setPrice(2499.0);
            Gadget g4 = new Gadget(); g4.setName("Dell XPS"); g4.setCategory("Laptop"); g4.setPrice(1500.0);
            Gadget g5 = new Gadget(); g5.setName("iPad Air"); g5.setCategory("Tablet"); g5.setPrice(599.0);
            Gadget g6 = new Gadget(); g6.setName("Sony XM5"); g6.setCategory("Accessories"); g6.setPrice(349.99);
            Gadget g7 = new Gadget(); g7.setName("Apple Watch"); g7.setCategory("Wearables"); g7.setPrice(399.0);
            Gadget g8 = new Gadget(); g8.setName("Google Pixel 8"); g8.setCategory("Smartphone"); g8.setPrice(699.0);
            Gadget g9 = new Gadget(); g9.setName("HP Spectre"); g9.setCategory("Laptop"); g9.setPrice(1300.0);
            Gadget g10 = new Gadget(); g10.setName("Kindle Paperwhite"); g10.setCategory("E-Reader"); g10.setPrice(149.99);

            gadgetRepository.saveAll(Arrays.asList(g1, g2, g3, g4, g5, g6, g7, g8, g9, g10));
            logger.info("Gadget seeding complete.");
        }
    }

    /**
     * Isolated endpoint for testing dynamic sorting and pagination.
     * Example: GET /api/scenario30b/gadgets?sortBy=price&direction=desc&page=0&size=5
     */
    @GetMapping("/gadgets")
    public Page<Gadget> getGadgets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        logger.info("Isolated Scenario 30B: Fetching gadgets. SortBy: {}, Direction: {}", sortBy, direction);
        
        return gadgetRepository.findAll(PageRequest.of(page, size, sort));
    }
}
