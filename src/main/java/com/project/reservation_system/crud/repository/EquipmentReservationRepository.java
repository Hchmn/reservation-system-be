package com.project.reservation_system.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.reservation_system.crud.entity.EquipmentReservation;

public interface EquipmentReservationRepository extends JpaRepository<EquipmentReservation, Long> {

}
