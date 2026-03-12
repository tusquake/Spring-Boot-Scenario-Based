package com.interview.debug.controller;

import com.interview.debug.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario10")
public class Scenario10Controller {

    @GetMapping("/test/bad-request")
    public String triggerBadRequest() {
        throw new IllegalArgumentException("Wait! You sent a bad request. Check your parameters.");
    }

    @GetMapping("/test/not-found")
    public String triggerNotFound() {
        throw new ResourceNotFoundException("Oops! That User/Order ID does not exist in our database.");
    }

    @GetMapping("/test/error")
    public String triggerServerError() {
        throw new RuntimeException("BOOM! Something went catastrophically wrong on our server.");
    }

    // THE BUILT-IN WAY: Using ResponseStatusException (No custom class needed)
    @GetMapping("/test/response-status")
    public String triggerResponseStatus() {
        throw new org.springframework.web.server.ResponseStatusException(
            org.springframework.http.HttpStatus.FORBIDDEN, "Access Denied by ResponseStatusException!");
    }
}
