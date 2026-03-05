package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.BarberDTO;
import com.jorgepozes04.barbershop_api.dto.BarberResponseDTO;
import com.jorgepozes04.barbershop_api.dto.WorkScheduleDTO;
import com.jorgepozes04.barbershop_api.service.BarberService;
import com.jorgepozes04.barbershop_api.service.WorkScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbers")
@AllArgsConstructor
@Slf4j
public class BarberController {
    private final BarberService barberService;
    private final WorkScheduleService scheduleService;

    /**
     * Create a work schedule for a barber
     */
    @PostMapping("/{id}/schedule")
    public ResponseEntity<WorkScheduleDTO> createSchedule(
            @PathVariable Long id,
            @RequestBody @Valid WorkScheduleDTO dto) {
        log.info("Creating schedule for barber ID: {}", id);
        WorkScheduleDTO savedSchedule = scheduleService.createSchedule(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
    }

    /**
     * Register a new barber
     */
    @PostMapping
    public ResponseEntity<BarberResponseDTO> createBarber(
            @RequestBody @Valid BarberDTO barberDTO) {
        log.info("Registering new barber with CPF: {}", barberDTO.getCpf());
        return ResponseEntity.status(HttpStatus.CREATED).body(barberService.register(barberDTO));
    }

    /**
     * Get all barbers
     */
    @GetMapping
    public ResponseEntity<List<BarberResponseDTO>> getAllBarbers() {
        log.debug("Fetching all barbers");
        return ResponseEntity.ok(barberService.getAllBarbers());
    }

    /**
     * Get barber by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> getBarberById(@PathVariable Long id) {
        log.debug("Fetching barber by ID: {}", id);
        BarberResponseDTO barber = barberService.getBarberById(id);
        return ResponseEntity.ok(barber);
    }

    /**
     * Get barber by CPF
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<BarberResponseDTO> getBarberByCpf(@PathVariable String cpf) {
        log.debug("Fetching barber by CPF: {}", cpf);
        BarberResponseDTO barber = barberService.getBarberByCpf(cpf);
        return ResponseEntity.ok(barber);
    }

    /**
     * Delete barber by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarberById(@PathVariable Long id) {
        log.info("Deleting barber with ID: {}", id);
        barberService.deleteBarberById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update barber information
     */
    @PutMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> updateBarber(
            @PathVariable Long id,
            @RequestBody @Valid BarberDTO barberDTO) {
        log.info("Updating barber with ID: {}", id);
        BarberResponseDTO updatedBarber = barberService.updateBarber(id, barberDTO);
        return ResponseEntity.ok(updatedBarber);
    }
}
