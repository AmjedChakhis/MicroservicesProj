package com.garage.vehicle_service.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDto {

    private String vin;

    private String registrationNumber;

    private String brand;

    private String model;

    private int year;

    private String color;

    private int mileage;

    private String fuelType;

    private LocalDate purchaseDate;

    private String ownerId;

    private String status;
}
