package com.garage.workshop_service.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
    private Long clientId;
    private String clientEmail;
    private String message;
}


