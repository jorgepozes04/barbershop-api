package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.ServiceOfferedDTO;
import com.jorgepozes04.barbershop_api.dto.mapper.ServiceOfferedMapper;
import com.jorgepozes04.barbershop_api.entities.ServiceOffered;
import com.jorgepozes04.barbershop_api.exception.ConflictException;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.ServiceOfferedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceOfferedService Tests")
class ServiceOfferedServiceTest {

    @Mock
    private ServiceOfferedRepository serviceOfferedRepository;

    @Mock
    private ServiceOfferedMapper serviceOfferedMapper;

    @InjectMocks
    private ServiceOfferedService serviceOfferedService;

    private ServiceOffered testService;
    private ServiceOfferedDTO testServiceDTO;

    @BeforeEach
    void setUp() {
        testService = new ServiceOffered();
        testService.setId(1L);
        testService.setName("Haircut");
        testService.setDescription("Professional haircut");
        testService.setPrice(BigDecimal.valueOf(50));
        testService.setDuration(30);

        testServiceDTO = new ServiceOfferedDTO();
        testServiceDTO.setName("Haircut");
        testServiceDTO.setDescription("Professional haircut");
        testServiceDTO.setPrice(BigDecimal.valueOf(50));
        testServiceDTO.setDuration(30);
    }

    @Test
    @DisplayName("Should create service successfully")
    void testCreateServiceSuccess() {
        // Arrange
        when(serviceOfferedRepository.findByNameIgnoreCase("Haircut")).thenReturn(Optional.empty());
        when(serviceOfferedRepository.save(any(ServiceOffered.class))).thenReturn(testService);
        when(serviceOfferedMapper.toDTO(testService)).thenReturn(testServiceDTO);

        // Act
        ServiceOfferedDTO result = serviceOfferedService.createService(testServiceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testServiceDTO.getName(), result.getName());
        verify(serviceOfferedRepository, times(1)).save(any(ServiceOffered.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when service already exists")
    void testCreateServiceAlreadyExists() {
        // Arrange
        when(serviceOfferedRepository.findByNameIgnoreCase("Haircut")).thenReturn(Optional.of(testService));

        // Act & Assert
        assertThrows(ConflictException.class, () -> serviceOfferedService.createService(testServiceDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when service name is null")
    void testCreateServiceWithNullName() {
        // Arrange
        testServiceDTO.setName(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> serviceOfferedService.createService(testServiceDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when service name is blank")
    void testCreateServiceWithBlankName() {
        // Arrange
        testServiceDTO.setName("");

        // Act & Assert
        assertThrows(ValidationException.class, () -> serviceOfferedService.createService(testServiceDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when price is null")
    void testCreateServiceWithNullPrice() {
        // Arrange
        testServiceDTO.setPrice(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> serviceOfferedService.createService(testServiceDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when price is negative")
    void testCreateServiceWithNegativePrice() {
        // Arrange
        testServiceDTO.setPrice(BigDecimal.valueOf(-10));

        // Act & Assert
        assertThrows(ValidationException.class, () -> serviceOfferedService.createService(testServiceDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when duration is null")
    void testCreateServiceWithNullDuration() {
        // Arrange
        testServiceDTO.setDuration(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> serviceOfferedService.createService(testServiceDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when duration is less than 15 minutes")
    void testCreateServiceWithInvalidDuration() {
        // Arrange
        testServiceDTO.setDuration(10);

        // Act & Assert
        assertThrows(ValidationException.class, () -> serviceOfferedService.createService(testServiceDTO));
    }

    @Test
    @DisplayName("Should get service by ID successfully")
    void testGetServiceByIdSuccess() {
        // Arrange
        Long id = 1L;
        when(serviceOfferedRepository.findById(id)).thenReturn(Optional.of(testService));
        when(serviceOfferedMapper.toDTO(testService)).thenReturn(testServiceDTO);

        // Act
        ServiceOfferedDTO result = serviceOfferedService.getServiceById(id);

        // Assert
        assertNotNull(result);
        assertEquals(testServiceDTO.getName(), result.getName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when service not found")
    void testGetServiceByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(serviceOfferedRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> serviceOfferedService.getServiceById(id));
    }

    @Test
    @DisplayName("Should get all services successfully")
    void testGetAllServicesSuccess() {
        // Arrange
        List<ServiceOffered> services = Arrays.asList(testService);
        when(serviceOfferedRepository.findAll()).thenReturn(services);
        when(serviceOfferedMapper.toDTO(testService)).thenReturn(testServiceDTO);

        // Act
        List<ServiceOfferedDTO> result = serviceOfferedService.getAllServices();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(serviceOfferedRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no services exist")
    void testGetAllServicesEmpty() {
        // Arrange
        when(serviceOfferedRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<ServiceOfferedDTO> result = serviceOfferedService.getAllServices();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should update service successfully")
    void testUpdateServiceSuccess() {
        // Arrange
        Long id = 1L;
        testServiceDTO.setPrice(BigDecimal.valueOf(60.0));

        when(serviceOfferedRepository.findById(id)).thenReturn(Optional.of(testService));
        when(serviceOfferedRepository.save(any(ServiceOffered.class))).thenReturn(testService);
        when(serviceOfferedMapper.toDTO(testService)).thenReturn(testServiceDTO);

        // Act
        ServiceOfferedDTO result = serviceOfferedService.updateService(id, testServiceDTO);

        // Assert
        assertNotNull(result);
        verify(serviceOfferedRepository, times(1)).save(any(ServiceOffered.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent service")
    void testUpdateServiceNotFound() {
        // Arrange
        Long id = 999L;
        when(serviceOfferedRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> serviceOfferedService.updateService(id, testServiceDTO));
    }

    @Test
    @DisplayName("Should delete service successfully")
    void testDeleteServiceSuccess() {
        // Arrange
        Long id = 1L;
        when(serviceOfferedRepository.findById(id)).thenReturn(Optional.of(testService));

        // Act
        assertDoesNotThrow(() -> serviceOfferedService.deleteService(id));

        // Assert
        verify(serviceOfferedRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent service")
    void testDeleteServiceNotFound() {
        // Arrange
        Long id = 999L;
        when(serviceOfferedRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> serviceOfferedService.deleteService(id));
    }
}
