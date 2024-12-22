package com.garage.workshop_service.dto;

import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
