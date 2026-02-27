package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.ServiceOfferedDTO;
import com.jorgepozes04.barbershop_api.entities.ServiceOffered;
import com.jorgepozes04.barbershop_api.repository.ServiceOfferedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ServiceOfferedService {
    private ServiceOfferedRepository serviceOfferedRepository;

    public ServiceOfferedDTO register(ServiceOfferedDTO serviceOfferedDTO) {
        ServiceOffered service = new ServiceOffered();
        service.setName(serviceOfferedDTO.getName());
        service.setPrice(serviceOfferedDTO.getPrice());
        service.setDuration(serviceOfferedDTO.getDuration());
        service.setDescription(serviceOfferedDTO.getDescription());
        ServiceOffered savedService = serviceOfferedRepository.save(service);
        return new ServiceOfferedDTO(savedService.getId(), savedService.getName(), savedService.getPrice(), savedService.getDuration(), savedService.getDescription());
    }
}
