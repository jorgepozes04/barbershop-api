package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.ClientDTO;
import com.jorgepozes04.barbershop_api.dto.mapper.ClientMapper;
import com.jorgepozes04.barbershop_api.entities.Client;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientService Tests")
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;
    private ClientDTO testClientDTO;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setCpf("12345678901");
        testClient.setName("John Doe");
        testClient.setPhoneNumber("11999999999");

        testClientDTO = new ClientDTO();
        testClientDTO.setId(1L);
        testClientDTO.setCpf("12345678901");
        testClientDTO.setName("John Doe");
        testClientDTO.setPhoneNumber("11999999999");
    }

    @Test
    @DisplayName("Should register client successfully")
    void testRegisterClientSuccess() {
        // Arrange
        when(clientMapper.toDTO(any(Client.class))).thenReturn(testClientDTO);
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // Act
        ClientDTO result = clientService.register(testClientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testClientDTO.getCpf(), result.getCpf());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Should get all clients with pagination")
    void testGetAllClientsSuccess() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Client> clients = Arrays.asList(testClient);
        Page<Client> clientPage = new PageImpl<>(clients);

        when(clientRepository.findAll(pageable)).thenReturn(clientPage);
        when(clientMapper.toDTO(testClient)).thenReturn(testClientDTO);

        // Act
        Page<ClientDTO> result = clientService.getAllClients(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(clientRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should get client by CPF successfully")
    void testGetClientByCpfSuccess() {
        // Arrange
        String cpf = "12345678901";
        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(testClient));
        when(clientMapper.toDTO(testClient)).thenReturn(testClientDTO);

        // Act
        ClientDTO result = clientService.getClientByCpf(cpf);

        // Assert
        assertNotNull(result);
        assertEquals(cpf, result.getCpf());
        verify(clientRepository, times(1)).findByCpf(cpf);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when CPF not found")
    void testGetClientByCpfNotFound() {
        // Arrange
        String cpf = "99999999999";
        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientByCpf(cpf));
    }

    @Test
    @DisplayName("Should delete client by ID successfully")
    void testDeleteClientByIdSuccess() {
        // Arrange
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.of(testClient));

        // Act
        assertDoesNotThrow(() -> clientService.deleteClientById(id));

        // Assert
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).delete(testClient);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent client")
    void testDeleteClientByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clientService.deleteClientById(id));
    }

    @Test
    @DisplayName("Should get client by ID successfully")
    void testGetClientByIdSuccess() {
        // Arrange
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.of(testClient));
        when(clientMapper.toDTO(testClient)).thenReturn(testClientDTO);

        // Act
        ClientDTO result = clientService.getClientById(id);

        // Assert
        assertNotNull(result);
        assertEquals(testClientDTO.getId(), result.getId());
        verify(clientRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getting non-existent client by ID")
    void testGetClientByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(id));
    }

    @Test
    @DisplayName("Should throw ValidationException when registering client with null CPF")
    void testRegisterClientWithNullCpf() {
        // Arrange
        testClientDTO.setCpf(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> clientService.register(testClientDTO));
    }

    @Test
    @DisplayName("Should return empty page when no clients exist")
    void testGetAllClientsEmpty() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> emptyPage = new PageImpl<>(Arrays.asList());

        when(clientRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<ClientDTO> result = clientService.getAllClients(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }
}
