package com.interview.debug.service;

import com.interview.debug.model.Scenario118User;
import com.interview.debug.repository.Scenario118UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Scenario118Service {

    private final Scenario118UserRepository userRepository;

    public Scenario118Service(Scenario118UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Scenario118User> getAllUsers() {
        return userRepository.findAll();
    }

    public Scenario118User createUser(Scenario118User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }
}
