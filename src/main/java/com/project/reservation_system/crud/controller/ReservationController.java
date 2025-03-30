package com.project.reservation_system.crud.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.reservation_system.crud.service.IReservationService;
import com.project.reservation_system.global.constant.Reservations;
import com.project.reservation_system.global.response.ApiResponse;
import com.project.reservation_system.global.response.DefaultResponse;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private IReservationService iReservationService;

    @GetMapping
    public ApiResponse<?> getReservation(@RequestParam(required = true) Reservations reservations,
            @RequestParam(required = true) Date date, Pageable pageable) {
        switch (reservations) {
            case AMENITIES:
                return DefaultResponse
                        .displayFoundObject(iReservationService.getAmenityReservationByDate(date, pageable));
            case EQUIPMENT:
                return DefaultResponse
                        .displayFoundObject(iReservationService.getEquipmentReservationByDate(date, pageable));
            default:
                return DefaultResponse.displayUnprocessable("Invalid reservation type");
        }
    }
}
