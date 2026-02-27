package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.BarberDTO;
import com.jorgepozes04.barbershop_api.dto.BarberResponseDTO;
import com.jorgepozes04.barbershop_api.entities.Barber;
import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.repository.BarberRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BarberService {
    private final BarberRepository barberRepository;
    private final PasswordEncoder passwordEncoder;

    public BarberDTO register(BarberDTO barberDTO) {
        Barber barber = new Barber();
        barber.setName(barberDTO.getName());

        UserCredentials credentials = barberDTO.getUserCredentials();
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        barber.setUserCredentials(credentials);
        credentials.setRole(Role.BARBER);

        barber.setUserCredentials(credentials);
        Barber savedBarber = barberRepository.save(barber);
        return new BarberDTO(savedBarber.getName(), savedBarber.getUserCredentials());
    }

    public List<BarberResponseDTO> getAllBarbers() {
        List<Barber> barbers = barberRepository.findAll();
        return barbers.stream()
                .map(barber -> new BarberResponseDTO(barber.getId(), barber.getName(), barber.getCpf()))
                .toList();
    }

    public BarberResponseDTO getBarberById(Long id) {
        Barber barber = barberRepository.findById(id).orElseThrow();
        return new BarberResponseDTO(barber.getId(), barber.getName(), barber.getCpf());
    }

    public BarberResponseDTO getBarberByCpf(String cpf) {
        Barber barber = barberRepository.findByCpf(cpf).orElseThrow();
        return new BarberResponseDTO(barber.getId(), barber.getName(), barber.getCpf());
    }

    public void deleteBarberById(Long id) {
        Barber barber = barberRepository.findById(id).orElseThrow();
        barberRepository.delete(barber);
    }

    public BarberResponseDTO updateBarber(Long id, @Valid BarberDTO barberDTO) {
        Barber barber = barberRepository.findById(id).orElseThrow();
        barber.setName(barberDTO.getName());
        Barber updatedBarber = barberRepository.save(barber);
        return new BarberResponseDTO(updatedBarber.getId(), updatedBarber.getName(), updatedBarber.getCpf());
    }
}
