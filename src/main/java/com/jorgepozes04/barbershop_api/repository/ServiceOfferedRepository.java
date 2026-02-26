package com.jorgepozes04.barbershop_api.repository;

import com.jorgepozes04.barbershop_api.entities.ServiceOffered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOfferedRepository extends JpaRepository<ServiceOffered, Long> {
}
