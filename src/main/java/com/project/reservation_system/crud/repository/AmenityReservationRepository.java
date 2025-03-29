package com.project.reservation_system.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.reservation_system.crud.entity.AmenityReservation;

public interface AmenityReservationRepository extends JpaRepository <AmenityReservation, Long> {
    
}
