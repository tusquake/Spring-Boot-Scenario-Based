package com.interview.debug.controller;

import com.interview.debug.model.Scenario21Employee;
import com.interview.debug.repository.Scenario21EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scenario21")
public class Scenario21Controller {

    private final Scenario21EmployeeRepository repository;

    public Scenario21Controller(Scenario21EmployeeRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initData() {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                new Scenario21Employee(null, "Alice", "IT", 75000.0, true),
                new Scenario21Employee(null, "Bob", "HR", 55000.0, true),
                new Scenario21Employee(null, "Charlie", "IT", 85000.0, false),
                new Scenario21Employee(null, "David", "Finance", 95000.0, true),
                new Scenario21Employee(null, "Eve", "HR", 45000.0, false)
            ));
        }
    }

    @GetMapping("/search")
    public List<Scenario21Employee> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) Double minSalary) {

        Specification<Scenario21Employee> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("name"), "%" + name + "%"));
        }

        if (dept != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("department"), dept));
        }

        if (minSalary != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("salary"), minSalary));
        }

        return repository.findAll(spec);
    }
}
