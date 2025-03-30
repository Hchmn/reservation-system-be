package com.project.reservation_system.crud.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.reservation_system.crud.dto.AmenityEquipmentReservationDTO;
import com.project.reservation_system.crud.entity.AmenityReservation;
import com.project.reservation_system.crud.entity.EquipmentReservation;
import com.project.reservation_system.crud.response.BothReservationResponse;
import com.project.reservation_system.global.constant.ReservationStatus;

public interface IReservationService {
    
    Page<AmenityReservation> getAmenityReservationByDate(Date dateToday, Pageable pageable);
    Page<EquipmentReservation> getEquipmentReservationByDate(Date dateToday, Pageable pageable);
    AmenityEquipmentReservationDTO createBothReservation(AmenityEquipmentReservationDTO amenityEquipmentReservationDTO);
    BothReservationResponse fetchBothReservation(Date startDate, Date endDate, ReservationStatus status);
}
