package com.project.reservation_system.crud.service;

import java.util.List;

import com.project.reservation_system.crud.dto.equipment.reservation.EquipmentReservationDTO;
import com.project.reservation_system.crud.entity.EquipmentReservation;

public interface IEquipmentReservationService {
    
    public List<EquipmentReservation> createEquipmentReservation(EquipmentReservationDTO equipmentReservationDTO);
}
