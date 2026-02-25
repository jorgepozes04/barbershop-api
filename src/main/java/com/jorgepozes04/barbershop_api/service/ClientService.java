package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.ClientDTO;
import com.jorgepozes04.barbershop_api.entities.Client;
import com.jorgepozes04.barbershop_api.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientDTO register(ClientDTO clientDTO) {
        Client client = new Client();
        return saveAndMapToDTO(clientDTO, client);
    }

    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(client -> {
            ClientDTO dto = new ClientDTO();
            dto.setName(client.getName());
            dto.setCpf(client.getCpf());
            dto.setPhoneNumber(client.getPhoneNumber());
            return dto;
        }).toList();
    }

    public ClientDTO getClientByCpf(String cpf) {
        Client client = clientRepository.findByCpf(cpf).orElseThrow();
        ClientDTO dto = new ClientDTO();
        dto.setName(client.getName());
        dto.setCpf(client.getCpf());
        dto.setPhoneNumber(client.getPhoneNumber());
        return dto;
    }

    public void deleteClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow();
        clientRepository.delete(client);
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow();
        ClientDTO dto = new ClientDTO();
        dto.setName(client.getName());
        dto.setCpf(client.getCpf());
        dto.setPhoneNumber(client.getPhoneNumber());
        return dto;
    }

    public ClientDTO updateClient(Long id, @Valid ClientDTO clientDTO) {
        Client client = clientRepository.findById(id).orElseThrow();
        return saveAndMapToDTO(clientDTO, client);
    }

    @NonNull
    private ClientDTO saveAndMapToDTO(@Valid ClientDTO clientDTO, Client client) {
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setPhoneNumber(clientDTO.getPhoneNumber());

        Client updatedClient = clientRepository.save(client);

        ClientDTO dto = new ClientDTO();
        dto.setName(updatedClient.getName());
        dto.setCpf(updatedClient.getCpf());
        dto.setPhoneNumber(updatedClient.getPhoneNumber());
        return dto;
    }
}