package com.jorgepozes04.barbershop_api.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jorgepozes04.barbershop_api.dto.AppointmentResponseDTO;
import com.jorgepozes04.barbershop_api.entities.Appointment;

/**
 * MapStruct mapper for Appointment entity to DTO conversions.
 * Handles the transformation from database entities to API response DTOs.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    /**
     * Converts an Appointment entity to AppointmentResponseDTO
     */
    default AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(appointment.getId());
        dto.setClientName(appointment.getClient().getName());
        dto.setClientPhoneNumber(appointment.getClient().getPhoneNumber());
        dto.setServiceName(appointment.getServiceOffered().getName());
        dto.setBarberName(appointment.getBarber().getName());
        dto.setBarbershopName(appointment.getBarber().getBarbershop().getName());
        dto.setDateTime(appointment.getStartTime());

        return dto;
    }

    /**
     * Converts a list of Appointment entities to list of AppointmentResponseDTOs
     */
    List<AppointmentResponseDTO> toResponseDTOs(List<Appointment> appointments);
}
