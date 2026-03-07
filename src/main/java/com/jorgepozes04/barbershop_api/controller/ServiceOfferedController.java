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

    @PostMapping
    public ResponseEntity<ServiceOfferedDTO> register(
            @RequestBody @Valid ServiceOfferedDTO serviceOfferedDTO) {
        log.info("Service registration: {}", serviceOfferedDTO.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.register(serviceOfferedDTO));
    }

    @GetMapping("/{barbershopId}")
    public ResponseEntity<List<ServiceOfferedDTO>> getAll(@PathVariable Long barbershopId) {
        return ResponseEntity.ok(serviceService.getAll(barbershopId));
    }
}
