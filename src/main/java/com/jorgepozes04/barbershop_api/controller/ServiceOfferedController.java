package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.ServiceOfferedDTO;
import com.jorgepozes04.barbershop_api.service.ServiceOfferedService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@AllArgsConstructor
public class ServiceOfferedController {
    private final ServiceOfferedService serviceService;

    @PostMapping
    public ResponseEntity<ServiceOfferedDTO> register(@RequestBody ServiceOfferedDTO serviceOfferedDTO){
        ServiceOfferedDTO serviceOffered = serviceService.register(serviceOfferedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceOffered);
    }

    @GetMapping("{barbershopId}")
    public ResponseEntity<List<ServiceOfferedDTO>> getAll(@PathVariable Long barbershopId){
        List<ServiceOfferedDTO> services = serviceService. getAll(barbershopId);
        return ResponseEntity.ok(services);

    }
}
