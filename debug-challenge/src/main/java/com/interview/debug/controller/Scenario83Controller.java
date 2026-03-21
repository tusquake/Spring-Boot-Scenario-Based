package com.interview.debug.controller;

import com.interview.debug.model.Scenario83Document;
import com.interview.debug.repository.Scenario83DocumentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenario83")
public class Scenario83Controller {

    private final Scenario83DocumentRepository documentRepository;

    public Scenario83Controller(Scenario83DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @PostMapping("/documents")
    public Scenario83Document createDocument(@RequestBody Scenario83Document doc, Authentication auth) {
        // Set the owner to current logged in user
        doc.setOwnerUsername(auth.getName());
        return documentRepository.save(doc);
    }

    // The MAGIC is here: hasPermission checks the CustomPermissionEvaluator
    @GetMapping("/documents/{id}")
    @PreAuthorize("hasPermission(#id, 'Scenario83Document', 'READ')")
    public Scenario83Document getDocument(@PathVariable Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @GetMapping("/documents")
    public List<Scenario83Document> getAll() {
        // This is a naive implementation; in real ABAC you would also filter the DB query
        return documentRepository.findAll();
    }
}
