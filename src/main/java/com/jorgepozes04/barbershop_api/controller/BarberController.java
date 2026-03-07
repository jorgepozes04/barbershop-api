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

    @PostMapping("/{id}/schedule")
    public ResponseEntity<WorkScheduleDTO> createSchedule(
            @PathVariable Long id,
            @RequestBody @Valid WorkScheduleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(id, dto));
    }

    @PostMapping
    public ResponseEntity<BarberResponseDTO> createBarber(@RequestBody @Valid BarberDTO barberDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(barberService.register(barberDTO));
    }

    @GetMapping
    public ResponseEntity<List<BarberResponseDTO>> getAllBarbers() {
        return ResponseEntity.ok(barberService.getAllBarbers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> getBarberById(@PathVariable Long id) {
        return ResponseEntity.ok(barberService.getBarberById(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<BarberResponseDTO> getBarberByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(barberService.getBarberByCpf(cpf));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarberById(@PathVariable Long id) {
        barberService.deleteBarberById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> updateBarber(
            @PathVariable Long id,
            @RequestBody @Valid BarberDTO barberDTO) {
        return ResponseEntity.ok(barberService.updateBarber(id, barberDTO));
    }
}
