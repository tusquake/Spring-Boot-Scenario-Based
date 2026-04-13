package com.interview.debug.scenario120;

import com.interview.debug.model.Scenario120User;
import com.interview.debug.repository.Scenario120UserRepository;
import com.interview.debug.service.Scenario120Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class Scenario120ServiceTest {

    @Mock
    private Scenario120UserRepository userRepository;

    @InjectMocks
    private Scenario120Service userService;

    @Test
    @DisplayName("registerUser: Should save user with ACTIVE status")
    void registerUser_ShouldSetStatusActive_AndSave() {
        // Given
        Scenario120User user = new Scenario120User("Tushar", null);
        given(userRepository.save(any(Scenario120User.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Scenario120User savedUser = userService.registerUser(user);

        // Then
        assertEquals("ACTIVE", savedUser.getStatus());
        then(userRepository).should(times(1)).save(any(Scenario120User.class));
    }

    @Test
    @DisplayName("registerUser: Should throw exception when username is empty")
    void registerUser_ShouldThrowException_WhenUsernameIsEmpty() {
        Scenario120User user = new Scenario120User("", null);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("deactivateUser: Should update status using ArgumentCaptor")
    void deactivateUser_ShouldUpdateStatusToInactive() {
        // Given
        Long userId = 1L;
        Scenario120User user = new Scenario120User("Tushar", "ACTIVE");
        user.setId(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        userService.deactivateUser(userId);

        // Then
        ArgumentCaptor<Scenario120User> userCaptor = ArgumentCaptor.forClass(Scenario120User.class);
        then(userRepository).should().save(userCaptor.capture());
        
        Scenario120User capturedUser = userCaptor.getValue();
        assertEquals("INACTIVE", capturedUser.getStatus());
        assertEquals("Tushar", capturedUser.getUsername());
    }

    @Test
    @DisplayName("deleteUser: Should call repository deleteById exactly once")
    void deleteUser_ShouldCallDeleteById() {
        // Given
        Long userId = 1L;

        // When
        userService.deleteUser(userId);

        // Then
        then(userRepository).should().deleteById(userId);
    }
}
