package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.AppointmentGuestDTO;
import com.jorgepozes04.barbershop_api.dto.AppointmentResponseDTO;
import com.jorgepozes04.barbershop_api.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@AllArgsConstructor
@Slf4j
public class AppointmentController {
    private final AppointmentService apService;

    /**
     * Get available time slots for a given barber, service, and date
     */
    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(
            @RequestParam Long barberID,
            @RequestParam Long serviceID,
            @RequestParam LocalDate date) {
        log.debug("Fetching available slots for barber {} on date {}", barberID, date);
        List<LocalTime> availableSlots = apService.getAvailableTimeSlots(barberID, serviceID, date);
        return ResponseEntity.ok(availableSlots);
    }

    /**
     * Book a new appointment
     */
    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentGuestDTO request) {
        log.info("Booking appointment for client: {}", request.getClientCpf());
        AppointmentResponseDTO appointment = apService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    /**
     * Get all appointments for a barber
     */
    @GetMapping("/{barberId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointmentsByBarbershop(
            @PathVariable Long barberId) {
        log.debug("Fetching all appointments for barber {}", barberId);
        List<AppointmentResponseDTO> appointments = apService.getAllAppointmentsByBarber(barberId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Get all appointments for a client
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByClientId(
            @PathVariable Long clientId) {
        log.debug("Fetching all appointments for client {}", clientId);
        List<AppointmentResponseDTO> appointments = apService.getAppointmentsByClientId(clientId);
        return ResponseEntity.ok(appointments);
    }
}
