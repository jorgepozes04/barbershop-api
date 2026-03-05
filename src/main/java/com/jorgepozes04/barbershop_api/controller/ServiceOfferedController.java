package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.ServiceOfferedDTO;
import com.jorgepozes04.barbershop_api.service.ServiceOfferedService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@AllArgsConstructor
@Slf4j
public class ServiceOfferedController {
    private final ServiceOfferedService serviceService;

    /**
     * Create a new service
     */
    @PostMapping
    public ResponseEntity<ServiceOfferedDTO> register(
            @RequestBody @Valid ServiceOfferedDTO serviceOfferedDTO) {
        log.info("Registering new service: {}", serviceOfferedDTO.getName());
        ServiceOfferedDTO serviceOffered = serviceService.register(serviceOfferedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceOffered);
    }

    /**
     * Get all services for a barbershop
     */
    @GetMapping("/{barbershopId}")
    public ResponseEntity<List<ServiceOfferedDTO>> getAll(
            @PathVariable Long barbershopId) {
        log.debug("Fetching all services for barbershop ID: {}", barbershopId);
        List<ServiceOfferedDTO> services = serviceService.getAll(barbershopId);
        return ResponseEntity.ok(services);
    }
}
