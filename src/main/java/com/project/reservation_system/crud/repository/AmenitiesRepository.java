package com.project.reservation_system.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.reservation_system.crud.entity.Amenities;

public interface AmenitiesRepository extends JpaRepository<Amenities, Long> {

}
