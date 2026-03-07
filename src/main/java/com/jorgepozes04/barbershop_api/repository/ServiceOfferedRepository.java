package com.jorgepozes04.barbershop_api.repository;

import com.jorgepozes04.barbershop_api.entities.ServiceOffered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOfferedRepository extends JpaRepository<ServiceOffered, Long> {
    List<ServiceOffered> findByBarbershopId(Long barbershopId);

    Object findByNameIgnoreCase(String string);
}
