package com.interview.debug.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
public class Scenario50Controller {

    private final Map<String, Project> projects = new ConcurrentHashMap<>();

    public Scenario50Controller() {
        projects.put("1", new Project("1", "Debug Challenge", "Java Interview Prep", "ACTIVE"));
        projects.put("2", new Project("2", "Loom Project", "Virtual Threads Research", "COMPLETED"));
    }

    // --- QUERIES ---

    @QueryMapping
    public Project projectById(@Argument String id) {
        return projects.get(id);
    }

    @QueryMapping
    public List<Project> allProjects() {
        return new ArrayList<>(projects.values());
    }

    // --- BATCH MAPPING (SOLVES N+1) ---
    // This is called once for the entire list of projects, not once per project!
    @BatchMapping
    public Map<Project, List<ProjectMember>> members(List<Project> projects) {
        System.out.println("--- BATCH LOADING MEMBERS FOR " + projects.size() + " PROJECTS ---");
        
        // Simulating a single batch database/service call
        return projects.stream().collect(Collectors.toMap(
                project -> project,
                project -> List.of(
                        new ProjectMember(UUID.randomUUID().toString(), "Dev for " + project.name(), "Developer"),
                        new ProjectMember(UUID.randomUUID().toString(), "QA for " + project.name(), "Tester")
                )
        ));
    }

    // --- MUTATIONS WITH VALIDATION ---

    @MutationMapping
    public Project addProject(@Argument @Valid ProjectInput input) {
        String id = UUID.randomUUID().toString();
        Project newProject = new Project(id, input.name(), input.description(), "CREATED");
        projects.put(id, newProject);
        return newProject;
    }

    // Records for Data
    public record Project(String id, String name, String description, String status) {}
    public record ProjectMember(String id, String name, String role) {}

    // DTO for Validation
    public record ProjectInput(
            @NotBlank(message = "Project name cannot be empty")
            @Size(min = 3, message = "Name must be at least 3 characters")
            String name,
            
            @NotBlank(message = "Description is required")
            String description
    ) {}
}
