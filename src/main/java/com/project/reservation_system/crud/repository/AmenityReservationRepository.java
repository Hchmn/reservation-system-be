package com.project.reservation_system.crud.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.reservation_system.crud.entity.Amenities;
import com.project.reservation_system.crud.entity.AmenityReservation;

public interface AmenityReservationRepository extends JpaRepository<AmenityReservation, Long> {

    @Query("SELECT ar FROM AmenityReservation ar WHERE ar.amenities = ?1 AND " +
            "( (ar.startDateTime BETWEEN ?2 AND ?3) OR " +
            "(ar.endDateTime BETWEEN ?2 AND ?3) OR " +
            "(ar.startDateTime <= ?2 AND ar.endDateTime >= ?3) )")
    List<AmenityReservation> checkAmenityReservation(Amenities amenity, LocalDateTime startDateTime,
            LocalDateTime endDateTime);
}
