package com.garage.vehicle_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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