package com.jorgepozes04.barbershop_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgepozes04.barbershop_api.dto.BarberDTO;
import com.jorgepozes04.barbershop_api.dto.BarberResponseDTO;
import com.jorgepozes04.barbershop_api.service.BarberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarberController.class)
@DisplayName("BarberController Tests")
class BarberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BarberService barberService;

    @Autowired
    private ObjectMapper objectMapper;

    private BarberDTO testBarberDTO;
    private BarberResponseDTO testBarberResponseDTO;

    @BeforeEach
    void setUp() {
        testBarberDTO = new BarberDTO();
        testBarberDTO.setName("Jane Smith");
        testBarberDTO.setCpf("98765432101");
        testBarberDTO.setUsername("jane.smith");
        testBarberDTO.setPassword("password123");

        testBarberResponseDTO = new BarberResponseDTO();
        testBarberResponseDTO.setId(1L);
        testBarberResponseDTO.setName("Jane Smith");
        testBarberResponseDTO.setCpf("98765432101");
    }

    @Test
    @DisplayName("Should register barber successfully")
    void testRegisterBarberSuccess() throws Exception {
        // Arrange
        when(barberService.register(any(BarberDTO.class))).thenReturn(testBarberResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/barbers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBarberDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Smith"));
    }

    @Test
    @DisplayName("Should return 400 when registering with invalid data")
    void testRegisterBarberWithInvalidData() throws Exception {
        // Arrange
        testBarberDTO.setName("");

        // Act & Assert
        mockMvc.perform(post("/barbers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBarberDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 when barber CPF already exists")
    void testRegisterBarberWithExistingCpf() throws Exception {
        // Arrange
        when(barberService.register(any(BarberDTO.class)))
                .thenThrow(new com.jorgepozes04.barbershop_api.exception.ConflictException("Barber already exists"));

        // Act & Assert
        mockMvc.perform(post("/barbers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBarberDTO)))
                .andExpect(status().isConflict());
    }
}
