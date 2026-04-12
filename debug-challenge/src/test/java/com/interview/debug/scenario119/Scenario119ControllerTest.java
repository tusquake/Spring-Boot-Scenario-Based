package com.interview.debug.scenario119;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.debug.controller.Scenario119Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class Scenario119ControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new Scenario119Controller()).build();
    }

    @Test
    void testHeader_ShouldReturnHeaderMessage() throws Exception {
        mockMvc.perform(get("/api/scenario119/header-test")
                        .header("X-Custom-Header", "Antigravity-AI")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Header received: Antigravity-AI"));
    }

    @Test
    void testParam_ShouldReturnDefaultName_WhenNoParamProvided() throws Exception {
        mockMvc.perform(get("/api/scenario119/param-test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Guest"));
    }

    @Test
    void testParam_ShouldReturnProvidedName() throws Exception {
        mockMvc.perform(get("/api/scenario119/param-test")
                        .param("name", "Tushar")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Tushar"));
    }

    @Test
    void testException_ShouldReturn400_WhenTriggered() throws Exception {
        // Note: standaloneSetup doesn't have the GlobalExceptionHandler by default 
        // unless you add it with .setControllerAdvice(). 
        // Without it, IllegalArgumentException will bubble up and result in 400/500 depending on setup.
        mockMvc.perform(get("/api/scenario119/exception-test")
                        .param("trigger", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Standalone maps IllegalArgumentException to 400 usually
    }

    @Test
    void complexJson_ShouldReturnCreatedAndProcessedJson() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 123);
        payload.put("data", "Sample Data");

        mockMvc.perform(post("/api/scenario119/complex-json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.data").value("Sample Data"))
                .andExpect(jsonPath("$.processed").value(true))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
