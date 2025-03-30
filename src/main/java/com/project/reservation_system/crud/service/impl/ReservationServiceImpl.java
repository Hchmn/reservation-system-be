package com.project.reservation_system.crud.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reservation_system.crud.entity.AmenityReservation;
import com.project.reservation_system.crud.entity.EquipmentReservation;
import com.project.reservation_system.crud.repository.AmenityReservationRepository;
import com.project.reservation_system.crud.repository.EquipmentReservationRepository;
import com.project.reservation_system.crud.service.IReservationService;

@Transactional
@Service
public class ReservationServiceImpl implements IReservationService {
    
    @Autowired
    private AmenityReservationRepository amenityReservationRepository;

    @Autowired
    private EquipmentReservationRepository equipmentReservationRepository;


    @Override
    public Page<AmenityReservation> getAmenityReservationByDate(Date dateToday, Pageable pageable){
        return amenityReservationRepository.findReservationsByDate(dateToday, pageable);
    }

    @Override
    public Page<EquipmentReservation> getEquipmentReservationByDate(Date dateToday, Pageable pageable){
        return equipmentReservationRepository.findReservationsByDate(dateToday, pageable);
    }

}
