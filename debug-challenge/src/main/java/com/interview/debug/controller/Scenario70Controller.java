package com.interview.debug.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenario70")
@Tag(name = "Scenario 70: Swagger Documentation", description = "Demonstrates how to document APIs using OpenAPI 3 annotations.")
public class Scenario70Controller {

    @Operation(summary = "Get a greeting message", description = "Returns a personalized greeting based on the provided name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the greeting"),
        @ApiResponse(responseCode = "400", description = "Invalid name provided")
    })
    @GetMapping("/greet")
    public String greet(
            @Parameter(description = "Name of the person to greet", example = "Tushar")
            @RequestParam(defaultValue = "Guest") String name) {
        return "Hello, " + name + "! Welcome to Scenario 70.";
    }

    @Operation(summary = "echo input data", description = "Echos back the string passed in the request body.")
    @PostMapping("/echo")
    public String echo(@RequestBody String data) {
        return "Echo: " + data;
    }
}
