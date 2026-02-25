package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.BarberDTO;
import com.jorgepozes04.barbershop_api.entities.Barber;
import com.jorgepozes04.barbershop_api.repository.BarberRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BarberService {
    private final BarberRepository barberRepository;

    public BarberDTO register(BarberDTO barberDTO) {
        Barber barber = new Barber();
        barber.setName(barberDTO.getName());
        Barber savedBarber = barberRepository.save(barber);
        return new BarberDTO(savedBarber.getId(), savedBarber.getName());
    }

    public List<BarberDTO> getAllBarbers() {
        List<Barber> barbers = barberRepository.findAll();
        return barbers.stream()
                .map(barber -> new BarberDTO(barber.getId(), barber.getName()))
                .toList();
    }

    public BarberDTO getBarberById(Long id) {
        Barber barber = barberRepository.findById(id).orElseThrow();
        return new BarberDTO(barber.getId(), barber.getName());
    }

    public BarberDTO getBarberByCpf(String cpf) {
        Barber barber = barberRepository.findByCpf(cpf).orElseThrow();
        return new BarberDTO(barber.getId(), barber.getName());
    }

    public void deleteBarberById(Long id) {
        Barber barber = barberRepository.findById(id).orElseThrow();
        barberRepository.delete(barber);
    }

    public BarberDTO updateBarber(Long id, @Valid BarberDTO barberDTO) {
        Barber barber = barberRepository.findById(id).orElseThrow();
        barber.setName(barberDTO.getName());
        Barber updatedBarber = barberRepository.save(barber);
        return new BarberDTO(updatedBarber.getId(), updatedBarber.getName());
    }
}
