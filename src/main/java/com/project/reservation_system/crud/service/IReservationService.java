package com.project.reservation_system.crud.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.reservation_system.crud.entity.AmenityReservation;
import com.project.reservation_system.crud.entity.EquipmentReservation;

public interface IReservationService {
    
    Page<AmenityReservation> getAmenityReservationByDate(Date dateToday, Pageable pageable);
    Page<EquipmentReservation> getEquipmentReservationByDate(Date dateToday, Pageable pageable);
}
