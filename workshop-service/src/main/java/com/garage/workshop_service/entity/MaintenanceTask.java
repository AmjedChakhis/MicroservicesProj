package com.garage.workshop_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private String startTime; // Format: "HH:mm"

    @Column(name = "end_time", nullable = false)
    private String endTime;   // Format: "HH:mm"

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status; // DONE or NOT_YET

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId; // ID from the VehicleService

    @Column(name = "task_date", nullable = false)
    private String date; // Format: "yyyy-MM-dd" for filtering by date
}
