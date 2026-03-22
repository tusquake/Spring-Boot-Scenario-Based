package com.interview.debug.controller;

import com.interview.debug.model.Scenario91Project;
import com.interview.debug.repository.Scenario91ProjectRepository;
import com.interview.debug.util.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenario91")
@RequiredArgsConstructor
@Slf4j
public class Scenario91Controller {

    private final Scenario91ProjectRepository projectRepository;

    @PostMapping("/projects")
    public Scenario91Project createProject(@RequestBody Scenario91Project project) {
        log.info("Creating project in tenant: {}", TenantContext.getTenantId());
        return projectRepository.save(project);
    }

    @GetMapping("/projects")
    public List<Scenario91Project> getAllProjects() {
        String tenantId = TenantContext.getTenantId();
        log.info("Fetching projects for tenant: {}", tenantId);
        return projectRepository.findAll();
    }

    @GetMapping("/current-tenant")
    public String getCurrentTenant() {
        return "Current Tenant: " + TenantContext.getTenantId();
    }
}
