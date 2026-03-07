package com.jorgepozes04.barbershop_api.dto.mapper;

import org.mapstruct.Mapper;

import com.jorgepozes04.barbershop_api.dto.ClientDTO;
import com.jorgepozes04.barbershop_api.entities.Client;

/**
 * MapStruct mapper for Client entity to DTO conversions.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {

    /**
     * Converts a Client entity to ClientDTO
     */
    default ClientDTO toDTO(Client client) {
        if (client == null) {
            return null;
        }

        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setCpf(client.getCpf());
        dto.setPhoneNumber(client.getPhoneNumber());

        return dto;
    }

    /**
     * Converts ClientDTO to Client entity
     */
    default Client toEntity(ClientDTO dto) {
        if (dto == null) {
            return null;
        }

        Client client = new Client();
        client.setId(dto.getId());
        client.setName(dto.getName());
        client.setCpf(dto.getCpf());
        client.setPhoneNumber(dto.getPhoneNumber());

        return client;
    }
}
