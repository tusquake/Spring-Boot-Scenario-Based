package com.interview.debug.scenario121;

import com.interview.debug.model.Scenario121Product;
import com.interview.debug.repository.Scenario121ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class Scenario121IntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private Scenario121ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void containerIsRunning() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldPersistAndRetrieveProduct() {
        // Given
        Scenario121Product product = Scenario121Product.builder()
                .name("Test Container Product")
                .price(new BigDecimal("99.99"))
                .category("Integration Test")
                .build();

        // When
        Scenario121Product savedProduct = productRepository.save(product);

        // Then
        assertThat(savedProduct.getId()).isNotNull();
        
        List<Scenario121Product> products = productRepository.findByCategory("Integration Test");
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Test Container Product");
    }
}
