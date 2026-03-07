package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.ServiceOfferedDTO;
import com.jorgepozes04.barbershop_api.dto.mapper.ServiceOfferedMapper;
import com.jorgepozes04.barbershop_api.entities.ServiceOffered;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.ServiceOfferedRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceOfferedService {
    private final ServiceOfferedRepository serviceOfferedRepository;
    private final ServiceOfferedMapper serviceOfferedMapper;

    /**
     * Creates and persists a new service offered.
     *
     * @param serviceOfferedDTO the service details
     * @return the created service
     * @throws ValidationException if validation fails
     */
    @Transactional
    public ServiceOfferedDTO register(ServiceOfferedDTO serviceOfferedDTO) {
        log.info("Registering new service: {}", serviceOfferedDTO.getName());
        validateServiceDTOInput(serviceOfferedDTO);

        ServiceOffered service = new ServiceOffered();
        service.setName(serviceOfferedDTO.getName());
        service.setPrice(serviceOfferedDTO.getPrice());
        service.setDuration(serviceOfferedDTO.getDuration());
        service.setDescription(serviceOfferedDTO.getDescription());

        ServiceOffered savedService = serviceOfferedRepository.save(service);
        log.info("Service registered successfully with ID: {}", savedService.getId());
        return serviceOfferedMapper.toDTO(savedService);
    }

    /**
     * Creates and persists a new service offered (alias for register).
     *
     * @param serviceOfferedDTO the service details
     * @return the created service
     * @throws ValidationException if validation fails
     */
    @Transactional
    public ServiceOfferedDTO createService(ServiceOfferedDTO serviceOfferedDTO) {
        return register(serviceOfferedDTO);
    }

    /**
     * Gets all services for a barbershop.
     *
     * @param barbershopId the barbershop ID
     * @return list of services
     */
    public List<ServiceOfferedDTO> getAll(Long barbershopId) {
        return serviceOfferedRepository.findByBarbershopId(barbershopId).stream()
                .map(serviceOfferedMapper::toDTO)
                .toList();
    }

    /**
     * Gets all services (without barbershop filter).
     *
     * @return list of all services
     */
    public List<ServiceOfferedDTO> getAllServices() {
        log.info("Retrieving all services");
        return serviceOfferedRepository.findAll().stream()
                .map(serviceOfferedMapper::toDTO)
                .toList();
    }

    /**
     * Gets a service by ID.
     *
     * @param serviceId the service ID
     * @return the service DTO
     * @throws ResourceNotFoundException if service not found
     */
    public ServiceOfferedDTO getServiceById(Long serviceId) {
        log.info("Retrieving service with ID: {}", serviceId);
        return serviceOfferedRepository.findById(serviceId)
                .map(serviceOfferedMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));
    }

    /**
     * Updates an existing service.
     *
     * @param serviceId         the service ID
     * @param serviceOfferedDTO the updated service details
     * @return the updated service DTO
     * @throws ResourceNotFoundException if service not found
     * @throws ValidationException       if validation fails
     */
    @Transactional
    public ServiceOfferedDTO updateService(Long serviceId, ServiceOfferedDTO serviceOfferedDTO) {
        log.info("Updating service with ID: {}", serviceId);
        validateServiceDTOInput(serviceOfferedDTO);

        ServiceOffered service = serviceOfferedRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));

        service.setName(serviceOfferedDTO.getName());
        service.setPrice(serviceOfferedDTO.getPrice());
        service.setDuration(serviceOfferedDTO.getDuration());
        service.setDescription(serviceOfferedDTO.getDescription());

        ServiceOffered updatedService = serviceOfferedRepository.save(service);
        log.info("Service updated successfully with ID: {}", serviceId);
        return serviceOfferedMapper.toDTO(updatedService);
    }

    /**
     * Deletes a service by ID.
     *
     * @param serviceId the service ID
     * @throws ResourceNotFoundException if service not found
     */
    @Transactional
    public void deleteService(Long serviceId) {
        log.info("Deleting service with ID: {}", serviceId);
        if (!serviceOfferedRepository.existsById(serviceId)) {
            throw new ResourceNotFoundException("Service not found with ID: " + serviceId);
        }
        serviceOfferedRepository.deleteById(serviceId);
        log.info("Service deleted successfully with ID: {}", serviceId);
    }

    private void validateServiceDTOInput(ServiceOfferedDTO serviceOfferedDTO) {
        if (serviceOfferedDTO == null) {
            throw new ValidationException("Service data cannot be null");
        }
        if (serviceOfferedDTO.getName() == null || serviceOfferedDTO.getName().isBlank()) {
            throw new ValidationException("Service name is required");
        }
        if (serviceOfferedDTO.getPrice() == null || serviceOfferedDTO.getPrice().signum() <= 0) {
            throw new ValidationException("Service price must be greater than zero");
        }
        if (serviceOfferedDTO.getDuration() == null || serviceOfferedDTO.getDuration() <= 0) {
            throw new ValidationException("Service duration must be greater than zero");
        }
    }
}
