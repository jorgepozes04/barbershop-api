package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.AppointmentGuestDTO;
import com.jorgepozes04.barbershop_api.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@AllArgsConstructor
public class AppointmentController {
    private final AppointmentService apService;

    @GetMapping("/available-slots")
    public List<LocalTime> getAvailableTimeSlots(Long barberID, Long serviceID, LocalDate date) {
        return apService.getAvailableTimeSlots(barberID, serviceID, date);
    }

    @PostMapping("/book")
    public ResponseEntity<AppointmentGuestDTO> createAppointment(@RequestBody AppointmentGuestDTO request) {
        try {
            apService.bookAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
