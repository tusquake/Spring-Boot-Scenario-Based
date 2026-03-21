package com.interview.debug.service;

import com.interview.debug.model.Scenario87Person;
import com.interview.debug.repository.Scenario87PersonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Scenario87Service {

    private static final Logger logger = LoggerFactory.getLogger(Scenario87Service.class);
    private final Scenario87PersonRepository personRepository;

    @Transactional
    public Scenario87Person createPerson(Scenario87Person person) {
        logger.info("[Scenario 87] Service: Saving Person. Cascading should save Address automatically.");
        // Because of CascadeType.ALL, we only need to save the parent.
        return personRepository.save(person);
    }

    @Transactional(readOnly = true)
    public Optional<Scenario87Person> getPerson(Long id) {
        logger.info("[Scenario 87] Service: Fetching Person with ID {}.", id);
        
        Optional<Scenario87Person> person = personRepository.findById(id);
        
        person.ifPresent(p -> {
            logger.info("[Scenario 87] Service: Person Loaded. Has Address been fetched? (Check SQL logs)");
            // If FetchType.LAZY is working, no ADDRESS SQL should have fired yet.
            // Accessing it now will trigger the proxy.
            logger.info("[Scenario 87] Service: Accessing Address Street: {}", p.getAddress().getStreet());
        });
        
        return person;
    }

    @Transactional
    public void deletePerson(Long id) {
        logger.info("[Scenario 87] Service: Deleting Person with ID {}. Cascading should delete Address.", id);
        personRepository.deleteById(id);
    }
}
