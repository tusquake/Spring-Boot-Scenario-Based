package com.interview.debug.service;

import com.interview.debug.model.IdempotencyRecord;
import com.interview.debug.repository.IdempotencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IdempotencyService {

    private final IdempotencyRepository repository;
    private final ObjectMapper objectMapper;

    public IdempotencyService(IdempotencyRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * Checks if a request has already been processed using the provided key.
     */
    public Optional<String> getResponse(String key) {
        return repository.findById(key).map(IdempotencyRecord::getResponseBody);
    }

    /**
     * Saves the response for a successfully processed request key.
     */
    @Transactional
    public void saveResponse(String key, Object response) {
        try {
            String json = objectMapper.writeValueAsString(response);
            repository.save(new IdempotencyRecord(key, json, 200));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize idempotency response", e);
        }
    }
}
