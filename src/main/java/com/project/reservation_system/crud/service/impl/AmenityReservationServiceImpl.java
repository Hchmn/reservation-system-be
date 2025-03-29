package com.project.reservation_system.crud.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reservation_system.crud.dto.amenity.reservation.AmenityReservationDTO;
import com.project.reservation_system.crud.entity.Amenities;
import com.project.reservation_system.crud.entity.AmenityReservation;
import com.project.reservation_system.crud.entity.Client;
import com.project.reservation_system.crud.repository.AmenitiesRepository;
import com.project.reservation_system.crud.repository.AmenityReservationRepository;
import com.project.reservation_system.crud.repository.ClientRepository;
import com.project.reservation_system.crud.service.IAmenityReservationService;
import com.project.reservation_system.global.constant.AmenityReservationStatus;

@Transactional
@Service
public class AmenityReservationServiceImpl implements IAmenityReservationService {

    @Autowired
    private AmenityReservationRepository amenityReservationRepository;

    @Autowired
    private AmenitiesRepository amenitiesRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public AmenityReservation createAmenityReservation(AmenityReservationDTO amenityReservationDTO) {

        Amenities amenities = amenitiesRepository.findById(amenityReservationDTO.getAmenityId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Amenities not found with ID: " + amenityReservationDTO.getAmenityId()));

        Client client = Client.builder()
                .address(amenityReservationDTO.getClient().getAddress())
                .contact(amenityReservationDTO.getClient().getContact())
                .email(amenityReservationDTO.getClient().getEmail())
                .firstName(amenityReservationDTO.getClient().getFirstName())
                .lastName(amenityReservationDTO.getClient().getLastName())
                .middleInitial(amenityReservationDTO.getClient().getMiddleName())
                .role(amenityReservationDTO.getClient().getEventRole())
                .build();

        client = clientRepository.save(client);

        AmenityReservation amenityReservation = AmenityReservation.builder()
                .amenities(amenities)
                .client(client)
                .endDateTime(amenityReservationDTO.getEndDateTime())
                .startDateTime(amenityReservationDTO.getStartDateTime())
                .purpose(amenityReservationDTO.getPurpose())
                .remarks(amenityReservationDTO.getRemarks())
                .status(amenityReservationDTO.getStatus())
                .build();

        return amenityReservationRepository.save(amenityReservation);
    }

    @Override
    public boolean isAmenityTaken(Long amenityId, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        Amenities amenity = amenitiesRepository.findById(amenityId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Amenities not found with ID: " + amenityId));

        // Query the repository to check if there are any overlapping reservations
        List<AmenityReservation> reservations = amenityReservationRepository.checkAmenityReservation(amenity,
                startDateTime, endDateTime);
        // If there are any reservations found, return true (the amenity is taken)
        return reservations.size() > 0;
    }

    @Override
    public AmenityReservation updateAmenityReservation(Long id, AmenityReservationDTO amenityReservationDTO){

        AmenityReservation amenityReservation = amenityReservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Amenitiy Reservation not found with ID: " + id)); 

        Amenities amenities = amenitiesRepository.findById(amenityReservationDTO.getAmenityId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Amenities not found with ID: " + amenityReservationDTO.getAmenityId()));
                        
        Client client = Client.builder()
                .id(amenityReservationDTO.getClient().getId())
                .address(amenityReservationDTO.getClient().getAddress())
                .contact(amenityReservationDTO.getClient().getContact())
                .email(amenityReservationDTO.getClient().getEmail())
                .firstName(amenityReservationDTO.getClient().getFirstName())
                .lastName(amenityReservationDTO.getClient().getLastName())
                .middleInitial(amenityReservationDTO.getClient().getMiddleName())
                .role(amenityReservationDTO.getClient().getEventRole())
                .build();
        client = clientRepository.save(client);

        amenityReservation = AmenityReservation.builder()
                .amenities(amenities)
                .client(client)
                .endDateTime(amenityReservationDTO.getEndDateTime())
                .startDateTime(amenityReservationDTO.getStartDateTime())
                .purpose(amenityReservationDTO.getPurpose())
                .remarks(amenityReservationDTO.getRemarks())
                .status(amenityReservationDTO.getStatus())
                .build();
        
        return amenityReservationRepository.save(amenityReservation);
    }

    @Override
    public Page<AmenityReservation> searchAmenityReservation(String keyword, AmenityReservationStatus amenityReservationStatus, Pageable pageable){
        return amenityReservationRepository.searchAmenityReservationsByKeyword(keyword, amenityReservationStatus.name(), pageable);
    }
}
