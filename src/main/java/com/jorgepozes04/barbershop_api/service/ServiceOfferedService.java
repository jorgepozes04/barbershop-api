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
     * Register a new service
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
     * Get all services for a barbershop
     */
    public List<ServiceOfferedDTO> getAll(Long barbershopId) {
        log.debug("Fetching all services for barbershop ID: {}", barbershopId);
        List<ServiceOffered> services = serviceOfferedRepository.findByBarbershopId(barbershopId);
        return services.stream()
                .map(serviceOfferedMapper::toDTO)
                .toList();
    }

    /**
     * Validate service DTO input
     */
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
