package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.BarberDTO;
import com.jorgepozes04.barbershop_api.dto.BarberResponseDTO;
import com.jorgepozes04.barbershop_api.service.BarberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbers")
@AllArgsConstructor
public class BarberController {
    private final BarberService barberService;

    @PostMapping
    public ResponseEntity<BarberDTO> createBarber(@RequestBody BarberDTO barberDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(barberService.register(barberDTO));
    }

    @GetMapping
    public ResponseEntity<List<BarberResponseDTO>> getAllBarbers() {
        return ResponseEntity.ok(barberService.getAllBarbers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> getBarberById(@PathVariable Long id) {
        BarberResponseDTO barber = barberService.getBarberById(id);
        if (barber != null) {
            return ResponseEntity.ok(barber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<BarberResponseDTO> getBarberByCpf(@PathVariable String cpf) {
        BarberResponseDTO barber = barberService.getBarberByCpf(cpf);
        if (barber != null) {
            return ResponseEntity.ok(barber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBarberById(@PathVariable Long id) {
        try {
            barberService.deleteBarberById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<BarberResponseDTO> updateBarber(@PathVariable Long id, @RequestBody @Valid BarberDTO barberDTO) {
        try {
            BarberResponseDTO updatedBarber = barberService.updateBarber(id, barberDTO);
            return ResponseEntity.ok(updatedBarber);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
