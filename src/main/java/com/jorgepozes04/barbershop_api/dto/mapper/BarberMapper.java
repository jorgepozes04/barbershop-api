package com.jorgepozes04.barbershop_api.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jorgepozes04.barbershop_api.dto.BarberResponseDTO;
import com.jorgepozes04.barbershop_api.entities.Barber;

@Mapper(componentModel = "spring")
public interface BarberMapper {

    default BarberResponseDTO toResponseDTO(Barber barber) {
        if (barber == null) {
            return null;
        }

        BarberResponseDTO dto = new BarberResponseDTO();
        dto.setId(barber.getId());
        dto.setName(barber.getName());
        dto.setCpf(barber.getCpf());

        return dto;
    }

    List<BarberResponseDTO> toResponseDTOs(List<Barber> barbers);
}
