package com.interview.debug.scenario118;

import com.interview.debug.model.Scenario118User;
import com.interview.debug.repository.Scenario118UserRepository;
import com.interview.debug.service.Scenario118Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private Scenario118UserRepository userRepository;

    @InjectMocks
    private Scenario118Service userService;

    @Test
    void createUser_ShouldSaveUser_WhenEmailIsUnique() {
        // Arrange
        Scenario118User user = new Scenario118User("Tushar", "tushar@example.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        // Act
        Scenario118User savedUser = userService.createUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("Tushar", savedUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        // Arrange
        Scenario118User user = new Scenario118User("Tushar", "tushar@example.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }
}
