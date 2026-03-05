package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.ClientDTO;
import com.jorgepozes04.barbershop_api.service.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@AllArgsConstructor
@Slf4j
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> registerClient(@RequestBody @Valid ClientDTO clientDTO) {
        log.info("Registering new client with CPF: {}", clientDTO.getCpf());
        ClientDTO createdClient = clientService.register(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> getAllClients(
            @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable) {
        log.debug("Fetching all clients with pagination");
        Page<ClientDTO> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        log.debug("Fetching client by ID: {}", id);
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClientDTO> getClientByCpf(@PathVariable String cpf) {
        log.debug("Fetching client by CPF: {}", cpf);
        ClientDTO client = clientService.getClientByCpf(cpf);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@PathVariable Long id) {
        log.info("Deleting client with ID: {}", id);
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable Long id,
            @RequestBody @Valid ClientDTO clientDTO) {
        log.info("Updating client with ID: {}", id);
        ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }
}