package com.interview.debug.controller;

import com.interview.debug.model.Scenario118User;
import com.interview.debug.service.Scenario118Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenario118/users")
public class Scenario118Controller {

    private final Scenario118Service userService;

    public Scenario118Controller(Scenario118Service userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Scenario118User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Scenario118User> createUser(@RequestBody Scenario118User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}
