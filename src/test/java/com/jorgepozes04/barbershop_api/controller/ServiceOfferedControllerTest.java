package com.jorgepozes04.barbershop_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgepozes04.barbershop_api.dto.ServiceOfferedDTO;
import com.jorgepozes04.barbershop_api.service.ServiceOfferedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(ServiceOfferedController.class)
@DisplayName("ServiceOfferedController Tests")
class ServiceOfferedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceOfferedService serviceOfferedService;

    @Autowired
    private ObjectMapper objectMapper;

    private ServiceOfferedDTO testServiceDTO;

    @BeforeEach
    void setUp() {
        testServiceDTO = new ServiceOfferedDTO();
        testServiceDTO.setName("Haircut");
        testServiceDTO.setDescription("Professional haircut");
        testServiceDTO.setPrice(50.0);
        testServiceDTO.setDuration(30);
    }

    @Test
    @DisplayName("Should create service successfully")
    void testCreateServiceSuccess() throws Exception {
        // Arrange
        when(serviceOfferedService.createService(any(ServiceOfferedDTO.class))).thenReturn(testServiceDTO);

        // Act & Assert
        mockMvc.perform(post("/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testServiceDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Haircut"));
    }

    @Test
    @DisplayName("Should get all services successfully")
    void testGetAllServicesSuccess() throws Exception {
        // Arrange
        when(serviceOfferedService.getAllServices()).thenReturn(Arrays.asList(testServiceDTO));

        // Act & Assert
        mockMvc.perform(get("/services")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Haircut"));
    }

    @Test
    @DisplayName("Should get service by ID successfully")
    void testGetServiceByIdSuccess() throws Exception {
        // Arrange
        when(serviceOfferedService.getServiceById(1L)).thenReturn(testServiceDTO);

        // Act & Assert
        mockMvc.perform(get("/services/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Haircut"));
    }

    @Test
    @DisplayName("Should return 404 when service not found")
    void testGetServiceByIdNotFound() throws Exception {
        // Arrange
        when(serviceOfferedService.getServiceById(999L))
                .thenThrow(
                        new com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException("Service not found"));

        // Act & Assert
        mockMvc.perform(get("/services/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update service successfully")
    void testUpdateServiceSuccess() throws Exception {
        // Arrange
        testServiceDTO.setPrice(60.0);
        when(serviceOfferedService.updateService(eq(1L), any(ServiceOfferedDTO.class))).thenReturn(testServiceDTO);

        // Act & Assert
        mockMvc.perform(put("/services/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testServiceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(60.0));
    }

    @Test
    @DisplayName("Should delete service successfully")
    void testDeleteServiceSuccess() throws Exception {
        // Arrange
        doNothing().when(serviceOfferedService).deleteService(1L);

        // Act & Assert
        mockMvc.perform(delete("/services/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent service")
    void testDeleteServiceNotFound() throws Exception {
        // Arrange
        doThrow(new com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException("Service not found"))
                .when(serviceOfferedService).deleteService(999L);

        // Act & Assert
        mockMvc.perform(delete("/services/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
