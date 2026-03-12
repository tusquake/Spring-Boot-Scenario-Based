package com.interview.debug.controller;

import com.interview.debug.model.ValidationUser;
import com.interview.debug.validation.OnCreate;
import com.interview.debug.validation.OnUpdate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenario11")
public class Scenario11Controller {

    @PostMapping("/create")
    public String createUser(@Validated({OnCreate.class, jakarta.validation.groups.Default.class}) @RequestBody ValidationUser user) {
        return "User created successfully with password validation!";
    }

    @PutMapping("/update")
    public String updateUser(@Validated({OnUpdate.class, jakarta.validation.groups.Default.class}) @RequestBody ValidationUser user) {
        return "User updated successfully! (Password was optional)";
    }
}
