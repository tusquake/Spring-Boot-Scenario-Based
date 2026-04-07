package com.interview.debug.controller;

import com.interview.debug.model.Scenario110JacksonModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Scenario 110: Jackson Properties & Annotations Demo
 * This controller demonstrates how Spring Boot and Jackson handle JSON mapping.
 */
@RestController
@RequestMapping("/api/scenario110")
@Tag(name = "Scenario 110: Jackson Mapping", description = "Endpoints to demonstrate JSON serialization and mapping.")
public class Scenario110JacksonController {

    /**
     * Demonstrates Serialization: Java to JSON
     * Notice how snake_case mapping, date formatting, and null filtering are
     * applied.
     */
    @GetMapping("/jackson")
    @Operation(summary = "Get Jackson Sample", description = "Shows how @JsonProperty, @JsonIgnore, and @JsonFormat affect the output.")
    public Scenario110JacksonModel getJacksonSample() {
        return Scenario110JacksonModel.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john.doe@example.com")
                .internalSecret("SHHH_SENSITIVE_SECRET") // This will NOT appear in the JSON
                .createdAt(LocalDateTime.now())
                .nullableField(null) // This will NOT appear in the JSON due to @JsonInclude
                .build();
    }

    /**
     * Demonstrates Deserialization: JSON to Java
     * Jackson will automatically map incoming snake_case fields (e.g. full_name)
     * back into Java camelCase.
     */
    @PostMapping("/jackson")
    @Operation(summary = "Post Jackson Echo", description = "Echoes back the received object to show Jackson's mapping capability.")
    public Scenario110JacksonModel echoJackson(@RequestBody Scenario110JacksonModel input) {
        // We just return it back to show how it was mapped.
        // If the user sends "full_name": "Jane", it will correctly populate
        // input.fullName.
        return input;
    }
}
