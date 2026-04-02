package com.springai.learning.config;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class Scenario106Config {

    @Value("classpath:knowledge/rules.txt")
    private Resource rulesResource;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    public CommandLineRunner loadKnowledge(VectorStore vectorStore) {
        return args -> {
            TextReader reader = new TextReader(rulesResource);
            List<Document> documents = reader.get();
            vectorStore.add(documents);
            System.out.println("Knowledge base loaded: " + documents.size() + " documents added to VectorStore.");
        };
    }
}