package com.interview.debug.controller;

import com.interview.debug.model.Scenario88SchoolClass;
import com.interview.debug.model.Scenario88Student;
import com.interview.debug.service.Scenario88Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenario88")
@RequiredArgsConstructor
public class Scenario88Controller {

    private final Scenario88Service classService;

    @PostMapping("/class")
    public ResponseEntity<Scenario88SchoolClass> create(@RequestBody Scenario88SchoolClass schoolClass) {
        return ResponseEntity.ok(classService.createClassWithStudents(schoolClass));
    }

    @GetMapping("/class/{id}")
    public ResponseEntity<Scenario88SchoolClass> get(@PathVariable Long id) {
        return classService.getClass(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/class/{id}/students")
    public ResponseEntity<java.util.Set<com.interview.debug.model.Scenario88Student>> getStudents(
            @PathVariable Long id) {
        return ResponseEntity.ok(classService.getStudentsByClass(id));
    }

    @PostMapping("/class/{id}/students")
    public ResponseEntity<String> addStudents(@PathVariable Long id,
            @RequestBody java.util.List<com.interview.debug.model.Scenario88Student> students) {
        classService.addStudentsToClass(id, students);
        return ResponseEntity.ok("BULK_STUDENTS_ADDED");
    }

    @PostMapping("/class/{id}/student")
    public ResponseEntity<String> addStudent(@PathVariable Long id, @RequestBody Scenario88Student student) {
        classService.addStudentToClass(id, student);
        return ResponseEntity.ok("STUDENT_ADDED_TO_CLASS");
    }
}
