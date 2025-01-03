package com.garage.vehicle_service.dto;

import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    private String identityNumber;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
}

