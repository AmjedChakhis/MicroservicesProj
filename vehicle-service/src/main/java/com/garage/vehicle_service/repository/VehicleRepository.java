package com.garage.vehicle_service.repository;

import com.garage.vehicle_service.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}