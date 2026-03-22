package com.interview.debug.controller;

import com.interview.debug.model.Scenario89Course;
import com.interview.debug.model.Scenario89Enrollment;
import com.interview.debug.model.Scenario89Student;
import com.interview.debug.service.Scenario89Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario89")
@RequiredArgsConstructor
public class Scenario89Controller {

    private final Scenario89Service service;

    @PostMapping("/course")
    public ResponseEntity<Scenario89Course> createCourse(@RequestBody Scenario89Course course) {
        return ResponseEntity.ok(service.createCourse(course));
    }

    @PostMapping("/student")
    public ResponseEntity<Scenario89Student> createStudent(@RequestBody Scenario89Student student) {
        return ResponseEntity.ok(service.createStudent(student));
    }

    @PostMapping("/enroll")
    public ResponseEntity<Scenario89Enrollment> enroll(@RequestBody Map<String, Object> request) {
        Long studentId = Long.valueOf(request.get("studentId").toString());
        Long courseId = Long.valueOf(request.get("courseId").toString());
        String grade = request.getOrDefault("grade", "In-Progress").toString();

        return ResponseEntity.ok(service.enrollStudent(studentId, courseId, grade));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Scenario89Course>> getCourses() {
        return ResponseEntity.ok(service.getAllCourses());
    }

    @GetMapping("/students")
    public ResponseEntity<List<Scenario89Student>> getStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }
}
