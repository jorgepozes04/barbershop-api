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

    @Transactional
    public ClientDTO register(ClientDTO clientDTO) {
        log.info("Registering new client with CPF: {}", clientDTO.getCpf());
        validateClientDTOInput(clientDTO);
        return saveAndMapToDTO(clientDTO, new Client());
    }

    public Page<ClientDTO> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toDTO);
    }

    /**
     * Retrieves a client by CPF.
     *
     * @param cpf the client's CPF
     * @return the client data transfer object
     * @throws ResourceNotFoundException if client not found
     */
    public ClientDTO getClientByCpf(String cpf) {
        Client client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with CPF: " + cpf));
        return clientMapper.toDTO(client);
    }

    /**
     * Deletes a client by ID.
     *
     * @param id the client's ID
     * @throws ResourceNotFoundException if client not found
     */
    @Transactional
    public void deleteClientById(Long id) {
        log.info("Deleting client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
        clientRepository.delete(client);
    }

    /**
     * Retrieves a client by ID.
     *
     * @param id the client's ID
     * @return the client data transfer object
     * @throws ResourceNotFoundException if client not found
     */
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
        return clientMapper.toDTO(client);
    }

    /**
     * Updates client information.
     *
     * @param id        the client's ID
     * @param clientDTO the updated client data
     * @return the updated client data transfer object
     * @throws ResourceNotFoundException if client not found
     * @throws ValidationException       if validation fails
     */
    @Transactional
    public ClientDTO updateClient(Long id, @Valid ClientDTO clientDTO) {
        log.info("Updating client with ID: {}", id);
        validateClientDTOInput(clientDTO);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + id));
        return saveAndMapToDTO(clientDTO, client);
    }

    @Transactional
    private ClientDTO saveAndMapToDTO(@Valid ClientDTO clientDTO, Client client) {
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        return clientMapper.toDTO(clientRepository.save(client));
    }

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