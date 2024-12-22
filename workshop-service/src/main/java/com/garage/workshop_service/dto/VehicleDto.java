package com.garage.workshop_service.dto;

import lombok.Data;

@Data
public class VehicleDto {
    private Long id;
    private String vin;
    private String registrationNumber;
    private String brand;
    private String model;
}
