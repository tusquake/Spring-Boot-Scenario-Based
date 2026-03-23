package com.interview.debug.controller;

import com.interview.debug.model.Scenario95User;
import com.interview.debug.model.UserProjection;
import com.interview.debug.repository.Scenario95UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenario95")
@RequiredArgsConstructor
public class Scenario95Controller {

    private final Scenario95UserRepository userRepository;

    @PostMapping("/seed")
    public String seed() {
        userRepository.save(Scenario95User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .address("123 Main St, Springfield")
                .phoneNumber("555-0192")
                .bio("A very long bio that takes up a lot of memory in the database...")
                .profilePictureUrl("http://images.com/john.jpg")
                .build());
        
        userRepository.save(Scenario95User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .address("456 Oak Ave, Metropolis")
                .phoneNumber("555-0193")
                .bio("Another extensive biography for Jane Smith...")
                .profilePictureUrl("http://images.com/jane.jpg")
                .build());
                
        return "Seeded 2 users with full details.";
    }

    /**
     * Fetches FULL entities (Expensive SQL: SELECT *)
     */
    @GetMapping("/users/full")
    public List<Scenario95User> getFullUsers() {
        return userRepository.findAll();
    }

    /**
     * Fetches ONLY Projections (Optimized SQL: SELECT name, email)
     */
    @GetMapping("/users/projection")
    public List<UserProjection> getProjectedUsers() {
        return userRepository.findAllProjectedBy();
    }
}
