package com.interview.debug.service;

import com.interview.debug.model.Scenario120User;
import com.interview.debug.repository.Scenario120UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Scenario120Service {

    private final Scenario120UserRepository userRepository;

    public Scenario120Service(Scenario120UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Scenario120User registerUser(Scenario120User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        user.setStatus("ACTIVE");
        return userRepository.save(user);
    }

    public void deactivateUser(Long id) {
        Optional<Scenario120User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            Scenario120User user = userOpt.get();
            user.setStatus("INACTIVE");
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
