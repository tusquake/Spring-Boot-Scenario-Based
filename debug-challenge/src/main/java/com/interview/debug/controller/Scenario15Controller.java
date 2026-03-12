package com.interview.debug.controller;

import com.interview.debug.model.ProjectData;
import com.interview.debug.repository.ProjectDataRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scenario15")
public class Scenario15Controller {

    private final ProjectDataRepository repository;

    public Scenario15Controller(ProjectDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/list")
    public List<ProjectData> listProjects() {
        return repository.findAll();
    }
}
