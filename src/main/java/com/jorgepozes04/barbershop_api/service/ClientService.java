package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.ClientDTO;
import com.jorgepozes04.barbershop_api.dto.mapper.ClientMapper;
import com.jorgepozes04.barbershop_api.entities.Client;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.ClientRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    /**
     * Register a new client
     */
    @Transactional
    public ClientDTO register(ClientDTO clientDTO) {
        log.info("Registering new client with CPF: {}", clientDTO.getCpf());
        validateClientDTOInput(clientDTO);

        Client client = new Client();
        return saveAndMapToDTO(clientDTO, client);
    }

    /**
     * Get all clients with pagination
     */
    public Page<ClientDTO> getAllClients(Pageable pageable) {
        log.debug("Fetching all clients with pagination");
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(clientMapper::toDTO);
    }

    /**
     * Get client by CPF
     */
    public ClientDTO getClientByCpf(String cpf) {
        log.debug("Fetching client by CPF: {}", cpf);
        Client client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with CPF: " + cpf));
        return clientMapper.toDTO(client);
    }

    /**
     * Delete client by ID
     */
    @Transactional
    public void deleteClientById(Long id) {
        log.info("Deleting client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
        clientRepository.delete(client);
        log.info("Client deleted successfully with ID: {}", id);
    }

    /**
     * Get client by ID
     */
    public ClientDTO getClientById(Long id) {
        log.debug("Fetching client by ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
        return clientMapper.toDTO(client);
    }

    /**
     * Update client information
     */
    @Transactional
    public ClientDTO updateClient(Long id, @Valid ClientDTO clientDTO) {
        log.info("Updating client with ID: {}", id);
        validateClientDTOInput(clientDTO);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));

        return saveAndMapToDTO(clientDTO, client);
    }

    /**
     * Save client and map to DTO
     */
    @Transactional
    private ClientDTO saveAndMapToDTO(@Valid ClientDTO clientDTO, Client client) {
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setPhoneNumber(clientDTO.getPhoneNumber());

        Client updatedClient = clientRepository.save(client);
        log.debug("Client saved with ID: {}", updatedClient.getId());

        return clientMapper.toDTO(updatedClient);
    }

    /**
     * Validate client DTO input
     */
    private void validateClientDTOInput(ClientDTO clientDTO) {
        if (clientDTO == null) {
            throw new ValidationException("Client data cannot be null");
        }
        if (clientDTO.getName() == null || clientDTO.getName().isBlank()) {
            throw new ValidationException("Client name is required");
        }
        if (clientDTO.getCpf() == null || clientDTO.getCpf().isBlank()) {
            throw new ValidationException("CPF is required");
        }
        if (clientDTO.getPhoneNumber() == null || clientDTO.getPhoneNumber().isBlank()) {
            throw new ValidationException("Phone number is required");
        }
    }
}