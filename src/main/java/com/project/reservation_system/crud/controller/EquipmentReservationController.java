package com.project.reservation_system.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.reservation_system.crud.dto.equipment.reservation.EquipmentReservationDTO;
import com.project.reservation_system.crud.entity.EquipmentReservation;
import com.project.reservation_system.crud.service.IEquipmentReservationService;
import com.project.reservation_system.global.response.ApiResponse;
import com.project.reservation_system.global.response.DefaultResponse;

@RestController
@RequestMapping("/equipment-reservation")
public class EquipmentReservationController {

    @Autowired
    private IEquipmentReservationService iEquipmentReservationService;

    @PostMapping
    public ApiResponse<?> createEquipmentReservation(@RequestBody EquipmentReservationDTO equipmentReservationDTO) {
        try {
            List<EquipmentReservation> equipmentReservation = iEquipmentReservationService
                    .createEquipmentReservation(equipmentReservationDTO);
            return DefaultResponse.displayCreatedObject(equipmentReservation);

        } catch (Exception e) {
            return DefaultResponse.displayUnprocessable("Failed to create equipment reservation");
        }
    }
}
