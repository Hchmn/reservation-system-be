package com.project.reservation_system.crud.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.reservation_system.crud.dto.amenity.reservation.AmenityReservationDTO;
import com.project.reservation_system.crud.entity.AmenityReservation;

public interface IAmenityReservationService {
    public AmenityReservation createAmenityReservation(AmenityReservationDTO amenityReservationDTO);

    public boolean isAmenityTaken(Long amenityId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    public AmenityReservation updateAmenityReservation(Long id, AmenityReservationDTO amenityReservationDTO);

    public Page<AmenityReservation> searchAmenityReservation(String keyword, Pageable pageable);
}
