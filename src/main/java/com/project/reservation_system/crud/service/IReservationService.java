package com.project.reservation_system.crud.service;

import java.util.Date;

import com.project.reservation_system.crud.dto.AmenityEquipmentReservationDTO;
import com.project.reservation_system.crud.response.BothReservationResponse;
import com.project.reservation_system.global.constant.ReservationStatus;

public interface IReservationService {
    
    BothReservationResponse getBothReservationByDate(String dateToday);
    // Page<EquipmentReservation> getEquipmentReservationByDate(Date dateToday, Pageable pageable);
    AmenityEquipmentReservationDTO createBothReservation(AmenityEquipmentReservationDTO amenityEquipmentReservationDTO);
    BothReservationResponse fetchBothReservation(Date startDate, Date endDate, ReservationStatus status);
}
