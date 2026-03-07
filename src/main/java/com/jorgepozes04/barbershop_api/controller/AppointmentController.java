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
     * Retrieves available appointment slots for a barber.
     *
     * @param barberID the barber's ID
     * @param serviceID the service ID
     * @param date the appointment date
     * @return list of available start times
     */
    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(
            @RequestParam Long barberID,
            @RequestParam Long serviceID,
            @RequestParam LocalDate date) {
        List<LocalTime> availableSlots = apService.getAvailableTimeSlots(barberID, serviceID, date);
        return ResponseEntity.ok(availableSlots);
    }

    /**
     * Creates a new appointment booking.
     *
     * @param request the booking details
     * @return the created appointment
     */
    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentGuestDTO request) {
        log.info("Booking appointment for client: {}", request.getClientCpf());
        AppointmentResponseDTO appointment = apService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    /**
     * Retrieves all appointments for a barber.
     *
     * @param barberId the barber's ID
     * @return list of appointments
     */
    @GetMapping("/{barberId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointmentsByBarbershop(
            @PathVariable Long barberId) {
        List<AppointmentResponseDTO> appointments = apService.getAllAppointmentsByBarber(barberId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Retrieves all appointments for a client.
     *
     * @param clientId the client's ID
     * @return list of appointments
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByClientId(
            @PathVariable Long clientId) {
        List<AppointmentResponseDTO> appointments = apService.getAppointmentsByClientId(clientId);
        return ResponseEntity.ok(appointments);
    }
}
