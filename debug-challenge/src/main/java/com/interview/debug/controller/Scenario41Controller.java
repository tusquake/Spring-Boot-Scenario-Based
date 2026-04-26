package com.interview.debug.controller;

import com.interview.debug.dto.Scenario41EmployeeDTO;
import com.interview.debug.repository.Scenario41EmployeeDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scenario41")
public class Scenario41Controller {

    private final Scenario41EmployeeDao employeeDao;

    public Scenario41Controller(Scenario41EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @GetMapping("/search")
    public List<Scenario41EmployeeDTO> search(
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) Double minSalary) {
        
        return employeeDao.findEmployeesByCriteria(dept, minSalary);
    }
}
