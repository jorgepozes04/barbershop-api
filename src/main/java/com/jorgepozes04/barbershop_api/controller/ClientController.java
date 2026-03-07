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

    /**
     * Registers a new client.
     *
     * @param clientDTO the client details
     * @return the created client
     */
    @PostMapping
    public ResponseEntity<ClientDTO> registerClient(@RequestBody @Valid ClientDTO clientDTO) {
        log.info("Client registration: {}", clientDTO.getCpf());
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.register(clientDTO));
    }

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> getAllClients(
            @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable) {
        Page<ClientDTO> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    /**
     * Retrieves a client by ID.
     *
     * @param id the client's ID
     * @return the client details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    /**
     * Retrieves a client by CPF.
     *
     * @param cpf the client's CPF
     * @return the client details
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClientDTO> getClientByCpf(@PathVariable String cpf) {
        ClientDTO client = clientService.getClientByCpf(cpf);
        return ResponseEntity.ok(client);
    }

    /**
     * Deletes a client by ID.
     *
     * @param id the client's ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@PathVariable Long id) {
        log.info("Deleting client with ID: {}", id);
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates client information.
     *
     * @param id the client's ID
     * @param clientDTO the updated client data
     * @return the updated client
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable Long id,
            @RequestBody @Valid ClientDTO clientDTO) {
        log.info("Updating client with ID: {}", id);
        ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }
}