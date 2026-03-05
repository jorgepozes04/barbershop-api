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
     * Register a new barber with validation
     * 
     * @param barberDTO the barber registration data
     * @return the registered barber response
     * @throws ValidationException if input is invalid
     * @throws ConflictException   if barber already exists
     */
    @Transactional
    public BarberResponseDTO register(BarberDTO barberDTO) {
        log.info("Registering new barber with CPF: {}", barberDTO.getCpf());

        // Validate input
        validateBarberDTOInput(barberDTO);

        // Check if barber with same CPF already exists
        if (barberRepository.findByCpf(barberDTO.getCpf()).isPresent()) {
            log.warn("Attempt to register barber with existing CPF: {}", barberDTO.getCpf());
            throw new ConflictException("Barber with CPF " + barberDTO.getCpf() + " already exists");
        }

        Barber barber = new Barber();
        barber.setName(barberDTO.getName());
        barber.setCpf(barberDTO.getCpf());

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(barberDTO.getUsername());
        // FIX: Encode the password from DTO, not from credentials (which is null)
        credentials.setPassword(passwordEncoder.encode(barberDTO.getPassword()));
        credentials.setRole(Role.BARBER);

        barber.setUserCredentials(credentials);
        Barber savedBarber = barberRepository.save(barber);

        log.info("Barber registered successfully with ID: {}", savedBarber.getId());
        return barberMapper.toResponseDTO(savedBarber);
    }

    /**
     * Validate barber DTO input
     */
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
        log.debug("Fetching all barbers");
        List<Barber> barbers = barberRepository.findAll();
        return barbers.stream()
                .map(barberMapper::toResponseDTO)
                .toList();
    }

    public BarberResponseDTO getBarberById(Long id) {
        log.debug("Fetching barber by ID: {}", id);
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with ID: " + id));
        return barberMapper.toResponseDTO(barber);
    }

    public BarberResponseDTO getBarberByCpf(String cpf) {
        log.debug("Fetching barber by CPF: {}", cpf);
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
        log.info("Barber deleted successfully with ID: {}", id);
    }

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
