package com.garage.workshop_service.dto;

import com.garage.workshop_service.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceTaskDto {
    private Long id;
    private String startTime; // Format: "HH:mm"
    private String endTime;   // Format: "HH:mm"
    private String description;
    private TaskStatus status; // DONE or NOT_YET
    private Long vehicleId; // ID from the VehicleService
    private String date; // Format: "yyyy-MM-dd" for filtering by date
}
