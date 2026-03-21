package com.interview.debug.service;

import com.interview.debug.model.Scenario88SchoolClass;
import com.interview.debug.model.Scenario88Student;
import com.interview.debug.repository.Scenario88SchoolClassRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Scenario88Service {

    private static final Logger logger = LoggerFactory.getLogger(Scenario88Service.class);
    private final Scenario88SchoolClassRepository classRepository;

    @Transactional
    public Scenario88SchoolClass createClassWithStudents(Scenario88SchoolClass schoolClass) {
        logger.info("[Scenario 88] Service: Creating Class '{}' with {} students.", 
                schoolClass.getClassName(), schoolClass.getStudents().size());
        
        // Critically, we must ensure each student knows about the class for the FK to be saved.
        // Our helper method addStudent() handles this, but if the JSON comes in with a list,
        // we should iterate and set the parent reference manually if the user didn't use the helper.
        schoolClass.getStudents().forEach(student -> student.setSchoolClass(schoolClass));
        
        return classRepository.save(schoolClass);
    }

    @Transactional(readOnly = true)
    public Optional<Scenario88SchoolClass> getClass(Long id) {
        logger.info("[Scenario 88] Service: Fetching Class ID {}.", id);
        Optional<Scenario88SchoolClass> schoolClass = classRepository.findById(id);
        
        schoolClass.ifPresent(c -> {
            logger.info("[Scenario 88] Service: Class loaded. Students fetched? (Check SQL)");
            // Accessing the size triggers the LAZY load
            logger.info("[Scenario 88] Service: Total Students in Class: {}", c.getStudents().size());
        });
        
        return schoolClass;
    }

    @Transactional(readOnly = true)
    public Set<Scenario88Student> getStudentsByClass(Long classId) {
        logger.info("[Scenario 88] Service: Fetching students for Class ID {}.", classId);
        return classRepository.findById(classId)
                .map(Scenario88SchoolClass::getStudents)
                .orElse(new java.util.HashSet<>());
    }

    @Transactional
    public void addStudentsToClass(Long classId, java.util.List<Scenario88Student> students) {
        classRepository.findById(classId).ifPresent(c -> {
            students.forEach(c::addStudent);
            classRepository.save(c);
        });
    }

    @Transactional
    public void addStudentToClass(Long classId, Scenario88Student student) {
        classRepository.findById(classId).ifPresent(c -> {
            c.addStudent(student); // Uses our helper method
            classRepository.save(c);
        });
    }
}
