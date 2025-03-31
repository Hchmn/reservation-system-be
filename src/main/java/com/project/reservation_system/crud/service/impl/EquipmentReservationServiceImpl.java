package com.project.reservation_system.crud.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reservation_system.crud.dto.amenity.reservation.EquipmentDetailDTO;
import com.project.reservation_system.crud.dto.equipment.reservation.EquipmentReservationDTO;
import com.project.reservation_system.crud.dto.equipment.reservation.EquipmentReservationStatusCountDTO;
import com.project.reservation_system.crud.entity.Client;
import com.project.reservation_system.crud.entity.Equipment;
import com.project.reservation_system.crud.entity.EquipmentReservation;
import com.project.reservation_system.crud.entity.EquipmentReservationDetails;
import com.project.reservation_system.crud.repository.ClientRepository;
import com.project.reservation_system.crud.repository.EquipmentRepository;
import com.project.reservation_system.crud.repository.EquipmentReservationDetailsRepository;
import com.project.reservation_system.crud.repository.EquipmentReservationRepository;
import com.project.reservation_system.crud.service.IEquipmentReservationService;
import com.project.reservation_system.global.constant.EquipmentReservationStatus;

@Transactional
@Service
public class EquipmentReservationServiceImpl implements IEquipmentReservationService {

        @Autowired
        private EquipmentReservationRepository equipmentReservationRepository;

        @Autowired
        private EquipmentReservationDetailsRepository equipmentReservationDetailsRepository;

        @Autowired
        private EquipmentRepository equipmentRepository;

        @Autowired
        private ClientRepository clientRepository;

        @Override
        public EquipmentReservation createEquipmentReservation(EquipmentReservationDTO equipmentReservationDTO) {

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

                EquipmentReservation equipmentReservation = EquipmentReservation.builder()
                                .client(client)
                                .endDateTime(equipmentReservationDTO.getEndDateTime())
                                .startDateTime(equipmentReservationDTO.getStartDateTime())
                                .purpose(equipmentReservationDTO.getPurpose())
                                .status(equipmentReservationDTO.getStatus())
                                .build();

                equipmentReservation = equipmentReservationRepository.save(equipmentReservation);

                for (EquipmentDetailDTO equipmentDetailDTO : equipmentReservationDTO.getEquipments()) {
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

                return equipmentReservation;
        }

        public Page<EquipmentReservation> searchEquipmentReservations(String keyword,
                        EquipmentReservationStatus equipmentReservationStatus, Pageable pageable) {
                return equipmentReservationRepository.searchEquipmentReservationsByKeyword(
                                (keyword == null ? "" : keyword.toLowerCase()), equipmentReservationStatus.name(), pageable);
        }

        public List<EquipmentReservationStatusCountDTO> countDashboardStatuses() {
                List<Object[]> results = equipmentReservationRepository.countReservationsByStatus();

                return results.stream()
                                .map(obj -> new EquipmentReservationStatusCountDTO((String) obj[0], (Long) obj[1]))
                                .collect(Collectors.toList());
        }

        @Override
        public void deleteEquipmentReservation(Long id) {
                EquipmentReservation reservation = equipmentReservationRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Equipment reservation not found with ID: " + id));

                equipmentReservationRepository.delete(reservation);
        }

        @Override
        public EquipmentReservation updateEquipmentReservation(Long id,
                        EquipmentReservationDTO equipmentReservationDTO) {
                EquipmentReservation existingReservation = equipmentReservationRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Equipment reservation not found with ID: " + id));

                // Update Client Information
                Client client = existingReservation.getClient();
                client.setFirstName(equipmentReservationDTO.getClient().getFirstName());
                client.setLastName(equipmentReservationDTO.getClient().getLastName());
                client.setMiddleInitial(equipmentReservationDTO.getClient().getMiddleName());
                client.setContact(equipmentReservationDTO.getClient().getContact());
                client.setEmail(equipmentReservationDTO.getClient().getEmail());
                client.setRole(equipmentReservationDTO.getClient().getEventRole());
                client.setAddress(equipmentReservationDTO.getClient().getAddress());

                client = clientRepository.save(client);

                // Update Equipment Reservation Details
                existingReservation.setPurpose(equipmentReservationDTO.getPurpose());
                existingReservation.setStatus(equipmentReservationDTO.getStatus());
                existingReservation.setStartDateTime(equipmentReservationDTO.getStartDateTime());
                existingReservation.setEndDateTime(equipmentReservationDTO.getEndDateTime());

                // Restore the equipment quantity and delete all the equipment reservation details
             
                for (EquipmentReservationDetails equipmentReservationDetails : existingReservation
                                .getReservationDetails()) {
                        Equipment equipment = equipmentReservationDetails.getEquipment();
                        equipment.setQuantity(equipment.getQuantity() + equipmentReservationDetails.getQuantity());
                        equipmentRepository.save(equipment);
                        equipmentReservationDetailsRepository.delete(equipmentReservationDetails);
                }

                // Update Equipment and Reservation Details
                for (EquipmentDetailDTO equipmentDetailDTO : equipmentReservationDTO.getEquipments()) {
                        Equipment equipment = equipmentRepository.findById(equipmentDetailDTO.getEquipmentId())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "Equipment not found with ID: "
                                                                        + equipmentDetailDTO.getEquipmentId()));

                        equipment.setQuantity(equipment.getQuantity() - equipmentDetailDTO.getQuantity());
                        equipment = equipmentRepository.save(equipment);

                        EquipmentReservationDetails equipmentReservationDetails = EquipmentReservationDetails.builder()
                                        .equipment(equipment)
                                        .equipmentReservation(existingReservation)
                                        .quantity(equipmentDetailDTO.getQuantity())
                                        .build();

                        equipmentReservationDetailsRepository.save(equipmentReservationDetails);
                }

                return equipmentReservationRepository.save(existingReservation);
        }

}
