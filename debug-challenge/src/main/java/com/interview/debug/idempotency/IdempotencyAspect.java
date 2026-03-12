package com.interview.debug.idempotency;

import com.interview.debug.model.IdempotencyRecord;
import com.interview.debug.repository.IdempotencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Aspect
@Component
public class IdempotencyAspect {

    private final IdempotencyRepository repository;
    private final ObjectMapper objectMapper;

    public IdempotencyAspect(IdempotencyRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(idempotent)")
    public Object handleIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String key = request.getHeader(idempotent.headerName());

        if (key == null || key.isEmpty()) {
            return joinPoint.proceed(); // Or throw error if you want to enforce it
        }

        Optional<IdempotencyRecord> existingRecord = repository.findById(key);
        if (existingRecord.isPresent()) {
            IdempotencyRecord record = existingRecord.get();
            // In a real app, you might want to reconstruct the ResponseEntity properly
            return ResponseEntity.status(record.getResponseStatus())
                    .header("Idempotent-Replayed", "true")
                    .body(objectMapper.readTree(record.getResponseBody()));
        }

        Object result = joinPoint.proceed();

        if (result instanceof ResponseEntity) {
            ResponseEntity<?> response = (ResponseEntity<?>) result;
            String bodyJson = objectMapper.writeValueAsString(response.getBody());
            repository.save(new IdempotencyRecord(key, bodyJson, response.getStatusCode().value()));
        }

        return result;
    }
}
