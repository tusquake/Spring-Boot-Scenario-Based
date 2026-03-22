package com.interview.debug.service;

import com.interview.debug.model.Scenario89Course;
import com.interview.debug.model.Scenario89Enrollment;
import com.interview.debug.model.Scenario89Student;
import com.interview.debug.repository.Scenario89CourseRepository;
import com.interview.debug.repository.Scenario89EnrollmentRepository;
import com.interview.debug.repository.Scenario89StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scenario89Service {

    private final Scenario89CourseRepository courseRepository;
    private final Scenario89StudentRepository studentRepository;
    private final Scenario89EnrollmentRepository enrollmentRepository;

    public Scenario89Course createCourse(Scenario89Course course) {
        return courseRepository.save(course);
    }

    public Scenario89Student createStudent(Scenario89Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public Scenario89Enrollment enrollStudent(Long studentId, Long courseId, String grade) {
        Scenario89Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Scenario89Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Scenario89Enrollment enrollment = new Scenario89Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setGrade(grade);

        // Synchronize bidirectional relationship
        student.getEnrollments().add(enrollment);
        course.getEnrollments().add(enrollment);

        return enrollmentRepository.save(enrollment);
    }

    public List<Scenario89Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Scenario89Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
