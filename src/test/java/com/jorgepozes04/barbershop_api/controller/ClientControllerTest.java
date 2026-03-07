package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.ClientDTO;
import com.jorgepozes04.barbershop_api.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@DisplayName("ClientController Tests")
class ClientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ClientService clientService;

        private ClientDTO testClientDTO;

        @BeforeEach
        void setUp() {
                testClientDTO = new ClientDTO();
                testClientDTO.setId(1L);
                testClientDTO.setCpf("12345678901");
                testClientDTO.setName("John Doe");
                testClientDTO.setPhoneNumber("11999999999");
        }

        @Test
        @DisplayName("Should get all clients with pagination")
        void testGetAllClientsSuccess() throws Exception {
                // Arrange
                when(clientService.getAllClients(any()))
                                .thenReturn(new PageImpl<>(Arrays.asList(testClientDTO)));

                // Act & Assert
                mockMvc.perform(get("/clients?page=0&size=10")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].cpf").value("12345678901"));
        }

        @Test
        @DisplayName("Should get client by CPF successfully")
        void testGetClientByCpfSuccess() throws Exception {
                // Arrange
                when(clientService.getClientByCpf("12345678901")).thenReturn(testClientDTO);

                // Act & Assert
                mockMvc.perform(get("/clients/cpf/12345678901")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("John Doe"));
        }

        @Test
        @DisplayName("Should return 404 when client not found by CPF")
        void testGetClientByCpfNotFound() throws Exception {
                // Arrange
                when(clientService.getClientByCpf("99999999999"))
                                .thenThrow(new com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException(
                                                "Client not found"));

                // Act & Assert
                mockMvc.perform(get("/clients/cpf/99999999999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should get client by ID successfully")
        void testGetClientByIdSuccess() throws Exception {
                // Arrange
                when(clientService.getClientById(1L)).thenReturn(testClientDTO);

                // Act & Assert
                mockMvc.perform(get("/clients/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("Should return 404 when client not found by ID")
        void testGetClientByIdNotFound() throws Exception {
                // Arrange
                when(clientService.getClientById(999L))
                                .thenThrow(new com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException(
                                                "Client not found"));

                // Act & Assert
                mockMvc.perform(get("/clients/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should delete client successfully")
        void testDeleteClientSuccess() throws Exception {
                // Arrange
                doNothing().when(clientService).deleteClientById(1L);

                // Act & Assert
                mockMvc.perform(delete("/clients/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent client")
        void testDeleteClientNotFound() throws Exception {
                // Arrange
                doThrow(new com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException("Client not found"))
                                .when(clientService).deleteClientById(999L);

                // Act & Assert
                mockMvc.perform(delete("/clients/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }
}
