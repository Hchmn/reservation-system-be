package com.project.reservation_system.crud.dto.equipment;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private String name;
    private String type;
    private int quantity;
    private String category;
    private Date dateAcquired;
}
