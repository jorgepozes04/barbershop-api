package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.BarberDTO;
import com.jorgepozes04.barbershop_api.dto.BarberResponseDTO;
import com.jorgepozes04.barbershop_api.dto.mapper.BarberMapper;
import com.jorgepozes04.barbershop_api.entities.Barber;
import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.exception.ConflictException;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.BarberRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BarberService {
    private final BarberRepository barberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BarberMapper barberMapper;

    /**
     * Registers a new barber with authorization credentials.
     * 
     * @param barberDTO barber registration data (name, CPF, username, password)
     * @return registered barber with assigned ID
     * @throws ValidationException if data validation fails
     * @throws ConflictException   if CPF already registered
     */
    @Transactional
    public BarberResponseDTO register(BarberDTO barberDTO) {
        log.info("Barber registration initiated: {}", barberDTO.getCpf());
        validateBarberDTOInput(barberDTO);

        if (barberRepository.findByCpf(barberDTO.getCpf()).isPresent()) {
            throw new ConflictException("Barber with CPF " + barberDTO.getCpf() + " already exists");
        }

        Barber barber = new Barber();
        barber.setName(barberDTO.getName());
        barber.setCpf(barberDTO.getCpf());

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(barberDTO.getUsername());
        credentials.setPassword(passwordEncoder.encode(barberDTO.getPassword()));
        credentials.setRole(Role.BARBER);

        barber.setUserCredentials(credentials);
        Barber savedBarber = barberRepository.save(barber);

        log.info("Barber registration completed: {}", savedBarber.getId());
        return barberMapper.toResponseDTO(savedBarber);
    }

    private void validateBarberDTOInput(BarberDTO barberDTO) {
        if (barberDTO == null) {
            throw new ValidationException("Barber data cannot be null");
        }
        if (barberDTO.getName() == null || barberDTO.getName().isBlank()) {
            throw new ValidationException("Barber name is required");
        }
        if (barberDTO.getCpf() == null || barberDTO.getCpf().isBlank()) {
            throw new ValidationException("CPF is required");
        }
        if (barberDTO.getUsername() == null || barberDTO.getUsername().isBlank()) {
            throw new ValidationException("Username is required");
        }
        if (barberDTO.getPassword() == null || barberDTO.getPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
    }

    public List<BarberResponseDTO> getAllBarbers() {
        return barberRepository.findAll().stream()
                .map(barberMapper::toResponseDTO)
                .toList();
    }

    public BarberResponseDTO getBarberById(Long id) {
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with ID: " + id));
        return barberMapper.toResponseDTO(barber);
    }

    public BarberResponseDTO getBarberByCpf(String cpf) {
        Barber barber = barberRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with CPF: " + cpf));
        return barberMapper.toResponseDTO(barber);
    }

    @Transactional
    public void deleteBarberById(Long id) {
        log.info("Deleting barber with ID: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with ID: " + id));
        barberRepository.delete(barber);

    @Transactional
    public BarberResponseDTO updateBarber(Long id, @Valid BarberDTO barberDTO) {
        log.info("Updating barber with ID: {}", id);

        validateBarberDTOInput(barberDTO);

        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with ID: " + id));

        barber.setName(barberDTO.getName());
        barber.setCpf(barberDTO.getCpf());

        Barber updatedBarber = barberRepository.save(barber);
        log.info("Barber updated successfully with ID: {}", id);

        return barberMapper.toResponseDTO(updatedBarber);
    }
}
