package com.interview.debug.scenario118;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.debug.controller.Scenario118Controller;
import com.interview.debug.model.Scenario118User;
import com.interview.debug.service.Scenario118Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerWebMvcTest {

    private MockMvc mockMvc;

    @Mock
    private Scenario118Service userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new Scenario118Controller(userService)).build();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/scenario118/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createUser_ShouldReturnUser_WhenSuccessful() throws Exception {
        Scenario118User user = new Scenario118User("Tushar", "tushar@example.com");
        when(userService.createUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/scenario118/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tushar"))
                .andExpect(jsonPath("$.email").value("tushar@example.com"));
    }
}
