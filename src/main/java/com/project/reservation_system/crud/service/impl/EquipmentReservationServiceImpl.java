package com.project.reservation_system.crud.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reservation_system.crud.dto.amenity.reservation.EquipmentDetailDTO;
import com.project.reservation_system.crud.dto.equipment.reservation.EquipmentReservationDTO;
import com.project.reservation_system.crud.entity.Client;
import com.project.reservation_system.crud.entity.Equipment;
import com.project.reservation_system.crud.entity.EquipmentReservation;
import com.project.reservation_system.crud.repository.ClientRepository;
import com.project.reservation_system.crud.repository.EquipmentRepository;
import com.project.reservation_system.crud.repository.EquipmentReservationRepository;
import com.project.reservation_system.crud.service.IEquipmentReservationService;

@Transactional
@Service
public class EquipmentReservationServiceImpl implements IEquipmentReservationService {

    @Autowired
    private EquipmentReservationRepository equipmentReservationRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<EquipmentReservation> createEquipmentReservation(EquipmentReservationDTO equipmentReservationDTO) {

        Client client = Client.builder()
                .address(equipmentReservationDTO.getClient().getAddress())
                .contact(equipmentReservationDTO.getClient().getContact())
                .email(equipmentReservationDTO.getClient().getEmail())
                .firstName(equipmentReservationDTO.getClient().getFirstName())
                .lastName(equipmentReservationDTO.getClient().getLastName())
                .middleInitial(equipmentReservationDTO.getClient().getMiddleName())
                .role(equipmentReservationDTO.getClient().getEventRole())
                .build();

        client = clientRepository.save(client);

        List<EquipmentReservation> equipmentReservations = new ArrayList<>();
        for (EquipmentDetailDTO equipmentDetailDTO : equipmentReservationDTO.getEquipments()) {
            Equipment equipment = equipmentRepository.findById(equipmentDetailDTO.getEquipmentId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Equipment not found with ID: " + equipmentDetailDTO.getEquipmentId()));

            equipment.setQuantity(equipment.getQuantity() - equipmentDetailDTO.getQuantity());
            equipment = equipmentRepository.save(equipment);
            EquipmentReservation equipmentReservation = EquipmentReservation.builder()
                    .client(client)
                    .endDateTime(equipmentReservationDTO.getEndDateTime())
                    .startDateTime(equipmentReservationDTO.getStartDateTime())
                    .equipment(equipment)
                    .quantity(equipmentDetailDTO.getQuantity())
                    .purpose(equipmentReservationDTO.getPurpose())
                    .status(equipmentReservationDTO.getStatus())
                    .build();

            equipmentReservations.add(equipmentReservation);
        }

        return equipmentReservationRepository.saveAll(equipmentReservations);
    }
}
