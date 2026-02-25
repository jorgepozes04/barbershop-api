package com.jorgepozes04.barbershop_api.repository;

import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    UserCredentials findByUsername(String username);
}
