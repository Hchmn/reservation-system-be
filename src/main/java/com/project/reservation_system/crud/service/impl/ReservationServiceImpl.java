package com.project.reservation_system.crud.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reservation_system.crud.dto.AmenityEquipmentReservationDTO;
import com.project.reservation_system.crud.dto.amenity.reservation.EquipmentDetailDTO;
import com.project.reservation_system.crud.entity.Amenities;
import com.project.reservation_system.crud.entity.AmenityReservation;
import com.project.reservation_system.crud.entity.Client;
import com.project.reservation_system.crud.entity.Equipment;
import com.project.reservation_system.crud.entity.EquipmentReservation;
import com.project.reservation_system.crud.entity.EquipmentReservationDetails;
import com.project.reservation_system.crud.repository.AmenitiesRepository;
import com.project.reservation_system.crud.repository.AmenityReservationRepository;
import com.project.reservation_system.crud.repository.ClientRepository;
import com.project.reservation_system.crud.repository.EquipmentRepository;
import com.project.reservation_system.crud.repository.EquipmentReservationDetailsRepository;
import com.project.reservation_system.crud.repository.EquipmentReservationRepository;
import com.project.reservation_system.crud.service.IReservationService;

@Transactional
@Service
public class ReservationServiceImpl implements IReservationService {

    @Autowired
    private AmenityReservationRepository amenityReservationRepository;

    @Autowired
    private EquipmentReservationRepository equipmentReservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private AmenitiesRepository amenitiesRepository;

    @Autowired
    private EquipmentReservationDetailsRepository equipmentReservationDetailsRepository;

    @Override
    public Page<AmenityReservation> getAmenityReservationByDate(Date dateToday, Pageable pageable) {
        return amenityReservationRepository.findReservationsByDate(dateToday, pageable);
    }

    @Override
    public Page<EquipmentReservation> getEquipmentReservationByDate(Date dateToday, Pageable pageable) {
        return equipmentReservationRepository.findReservationsByDate(dateToday, pageable);
    }

    @Override
    public AmenityEquipmentReservationDTO createBothReservation(
            AmenityEquipmentReservationDTO amenityEquipmentReservationDTO) {

        Client client = Client.builder()
                .address(amenityEquipmentReservationDTO.getClient().getAddress())
                .contact(amenityEquipmentReservationDTO.getClient().getContact())
                .email(amenityEquipmentReservationDTO.getClient().getEmail())
                .firstName(amenityEquipmentReservationDTO.getClient().getFirstName())
                .lastName(amenityEquipmentReservationDTO.getClient().getLastName())
                .middleInitial(amenityEquipmentReservationDTO.getClient().getMiddleName())
                .role(amenityEquipmentReservationDTO.getClient().getEventRole())
                .build();
        client = clientRepository.save(client);

        Amenities amenities = amenitiesRepository
                .findById(amenityEquipmentReservationDTO.getAmenityReservationDTO().getAmenityId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Amenities not found with ID: "
                                + amenityEquipmentReservationDTO.getAmenityReservationDTO().getAmenityId()));

        AmenityReservation amenityReservation = AmenityReservation.builder()
                .amenities(amenities)
                .client(client)
                .endDateTime(amenityEquipmentReservationDTO.getAmenityReservationDTO().getEndDateTime())
                .startDateTime(amenityEquipmentReservationDTO.getAmenityReservationDTO().getStartDateTime())
                .purpose(amenityEquipmentReservationDTO.getAmenityReservationDTO().getPurpose())
                .remarks(amenityEquipmentReservationDTO.getAmenityReservationDTO().getRemarks())
                .status(amenityEquipmentReservationDTO.getAmenityReservationDTO().getStatus())
                .build();

        amenityReservationRepository.save(amenityReservation);

        EquipmentReservation equipmentReservation = EquipmentReservation.builder()
                .client(client)
                .endDateTime(amenityEquipmentReservationDTO.getEquipmentReservationDTO().getEndDateTime())
                .startDateTime(amenityEquipmentReservationDTO.getEquipmentReservationDTO().getStartDateTime())
                .purpose(amenityEquipmentReservationDTO.getEquipmentReservationDTO().getPurpose())
                .status(amenityEquipmentReservationDTO.getEquipmentReservationDTO().getStatus())
                .build();

        equipmentReservation = equipmentReservationRepository.save(equipmentReservation);

        for (EquipmentDetailDTO equipmentDetailDTO : amenityEquipmentReservationDTO.getEquipmentReservationDTO()
                .getEquipments()) {
            Equipment equipment = equipmentRepository.findById(equipmentDetailDTO.getEquipmentId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Equipment not found with ID: "
                                    + equipmentDetailDTO.getEquipmentId()));

            equipment.setQuantity(equipment.getQuantity() - equipmentDetailDTO.getQuantity());
            equipment = equipmentRepository.save(equipment);

            EquipmentReservationDetails equipmentReservationDetails = EquipmentReservationDetails.builder()
                    .equipment(equipment)
                    .equipmentReservation(equipmentReservation)
                    .quantity(equipmentDetailDTO.getQuantity())
                    .build();

            equipmentReservationDetailsRepository.save(equipmentReservationDetails);

        }
        return amenityEquipmentReservationDTO;
    }

}
