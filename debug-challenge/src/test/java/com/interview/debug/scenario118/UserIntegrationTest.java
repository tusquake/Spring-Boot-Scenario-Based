package com.interview.debug.scenario118;

import com.interview.debug.model.Scenario118User;
import com.interview.debug.repository.Scenario118UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Scenario118UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_ShouldPersistInDatabase() {
        // Arrange
        Scenario118User user = new Scenario118User("Tushar Integration", "tushar.int@example.com");

        // Act
        // Note: Using withBasicAuth or similar if security is enabled, 
        // but here we just test the end-to-end flow. 
        // For simplicity, we assume the API is accessible for this test or configured for test.
        ResponseEntity<Scenario118User> response = restTemplate
                .withBasicAuth("admin", "password") // Default credentials often used in such setups
                .postForEntity("/api/scenario118/users", user, Scenario118User.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        
        // Verify database state
        assertEquals(1, userRepository.count());
        assertEquals("Tushar Integration", userRepository.findAll().get(0).getName());
    }
}
