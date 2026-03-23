package com.interview.debug.service;

import com.interview.debug.model.Scenario94Identity;
import com.interview.debug.model.Scenario94Sequence;
import com.interview.debug.model.Scenario94UUID;
import com.interview.debug.model.Scenario94Auto;
import com.interview.debug.repository.Scenario94IdentityRepository;
import com.interview.debug.repository.Scenario94SequenceRepository;
import com.interview.debug.repository.Scenario94UUIDRepository;
import com.interview.debug.repository.Scenario94AutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Scenario94Service {
    private final Scenario94IdentityRepository identityRepository;
    private final Scenario94SequenceRepository sequenceRepository;
    private final Scenario94UUIDRepository uuidRepository;
    private final Scenario94AutoRepository autoRepository;

    @Transactional
    public long testBatchPerformanceIdentity(int count) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            identityRepository.save(Scenario94Identity.builder().name("Item " + i).build());
        }
        long end = System.currentTimeMillis();
        log.info("IDENTITY Strategy: Saved {} items in {} ms", count, (end - start));
        return (end - start);
    }

    @Transactional
    public long testBatchPerformanceSequence(int count) {
        long start = System.currentTimeMillis();
        // SEQUENCE allows pre-allocating IDs, so Hibernate can batch these!
        List<Scenario94Sequence> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(Scenario94Sequence.builder().name("Item " + i).build());
        }
        sequenceRepository.saveAll(items);
        long end = System.currentTimeMillis();
        log.info("SEQUENCE Strategy: Saved {} items in {} ms", count, (end - start));
        return (end - start);
    }

    @Transactional
    public long testBatchPerformanceUUID(int count) {
        long start = System.currentTimeMillis();
        List<Scenario94UUID> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(Scenario94UUID.builder().name("Item " + i).build());
        }
        uuidRepository.saveAll(items);
        long end = System.currentTimeMillis();
        log.info("UUID Strategy: Saved {} items in {} ms", count, (end - start));
        return (end - start);
    }

    @Transactional
    public long testBatchPerformanceAuto(int count) {
        long start = System.currentTimeMillis();
        List<Scenario94Auto> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(Scenario94Auto.builder().name("Item " + i).build());
        }
        autoRepository.saveAll(items);
        long end = System.currentTimeMillis();
        log.info("AUTO Strategy: Saved {} items in {} ms", count, (end - start));
        return (end - start);
    }
}
