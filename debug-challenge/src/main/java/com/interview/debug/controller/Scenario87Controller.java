package com.interview.debug.controller;

import com.interview.debug.model.Scenario87Person;
import com.interview.debug.service.Scenario87Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario87")
@RequiredArgsConstructor
public class Scenario87Controller {

    private final Scenario87Service personService;

    @PostMapping("/person")
    public ResponseEntity<Scenario87Person> create(@RequestBody Scenario87Person person) {
        return ResponseEntity.ok(personService.createPerson(person));
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Scenario87Person> get(@PathVariable Long id) {
        return personService.getPerson(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/person/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok(Map.of("message", "PERSON_AND_ADDRESS_DELETED"));
    }
}
