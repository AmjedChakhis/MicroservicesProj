package com.garage.workshop_service.service;

import com.garage.workshop_service.config.RabbitMQConfig;
import com.garage.workshop_service.dto.MaintenanceTaskDto;
import com.garage.workshop_service.entity.MaintenanceTask;
import com.garage.workshop_service.entity.TaskStatus;
import com.garage.workshop_service.repository.MaintenanceTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaintenanceTaskService {

    @Autowired
    private MaintenanceTaskRepository taskRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String VEHICLE_SERVICE_URL = "http://localhost:8082/api/vehicles";

    // Create a new maintenance task
    public MaintenanceTaskDto createTask(MaintenanceTaskDto taskDto) {
        // Validate vehicle ID
        validateVehicleId(taskDto.getVehicleId());

        MaintenanceTask task = convertToEntity(taskDto);
        MaintenanceTask savedTask = taskRepository.save(task);

        return convertToDto(savedTask);
    }

    // Get all maintenance tasks
    public List<MaintenanceTaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get a maintenance task by ID
    public MaintenanceTaskDto getTaskById(Long id) {
        MaintenanceTask task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MaintenanceTask not found"));
        return convertToDto(task);
    }

    // Get tasks by date
    public List<MaintenanceTaskDto> getTasksByDate(String date) {
        return taskRepository.findByDate(date)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Update a maintenance task
    public MaintenanceTaskDto updateTask(Long id, MaintenanceTaskDto updatedTaskDto) {
        MaintenanceTask existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MaintenanceTask not found"));

        // Update task fields
        existingTask.setStartTime(updatedTaskDto.getStartTime());
        existingTask.setEndTime(updatedTaskDto.getEndTime());
        existingTask.setDescription(updatedTaskDto.getDescription());
        existingTask.setStatus(updatedTaskDto.getStatus());
        existingTask.setVehicleId(updatedTaskDto.getVehicleId());
        existingTask.setDate(updatedTaskDto.getDate());

        // Validate vehicle ID for updates
        validateVehicleId(updatedTaskDto.getVehicleId());

        MaintenanceTask savedTask = taskRepository.save(existingTask);

        if ( updatedTaskDto.getStatus() == TaskStatus.SCHEDULED) {
            System.out.println("Publishing message for vehicleId: " + updatedTaskDto.getVehicleId());
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    Map.of("vehicleId", updatedTaskDto.getVehicleId())
            );
        } else {
            System.out.println("Message not published. Status didn't change or is not SCHEDULED."  );
        }


        return convertToDto(savedTask);
    }


    // Delete a maintenance task
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Validate vehicle ID via VehicleService
    private void validateVehicleId(Long vehicleId) {
        String url = VEHICLE_SERVICE_URL + "/" + vehicleId;

        try {
            webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> Mono.error(new IllegalArgumentException("Invalid Vehicle ID"))
                    )
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            throw new IllegalArgumentException("Vehicle validation failed: " + e.getMessage());
        }
    }

    // Convert DTO to entity
    private MaintenanceTask convertToEntity(MaintenanceTaskDto dto) {
        return MaintenanceTask.builder()
                .id(dto.getId())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .vehicleId(dto.getVehicleId())
                .date(dto.getDate())
                .build();
    }

    // Convert entity to DTO
    private MaintenanceTaskDto convertToDto(MaintenanceTask task) {
        return MaintenanceTaskDto.builder()
                .id(task.getId())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .description(task.getDescription())
                .status(task.getStatus())
                .vehicleId(task.getVehicleId())
                .date(task.getDate())
                .build();
    }
}
