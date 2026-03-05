package com.jorgepozes04.barbershop_api.dto.mapper;

import org.mapstruct.Mapper;

import com.jorgepozes04.barbershop_api.dto.ServiceOfferedDTO;
import com.jorgepozes04.barbershop_api.entities.ServiceOffered;

@Mapper(componentModel = "spring")
public interface ServiceOfferedMapper {

    default ServiceOfferedDTO toDTO(ServiceOffered service) {
        if (service == null) {
            return null;
        }

        ServiceOfferedDTO dto = new ServiceOfferedDTO();
        dto.setName(service.getName());
        dto.setPrice(service.getPrice());
        dto.setDuration(service.getDuration());
        dto.setDescription(service.getDescription());

        return dto;
    }

    default ServiceOffered toEntity(ServiceOfferedDTO dto) {
        if (dto == null) {
            return null;
        }

        ServiceOffered service = new ServiceOffered();
        service.setName(dto.getName());
        service.setPrice(dto.getPrice());
        service.setDuration(dto.getDuration());
        service.setDescription(dto.getDescription());

        return service;
    }
}
