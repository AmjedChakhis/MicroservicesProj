package com.garage.vehicle_service.service;

import com.garage.vehicle_service.dto.ClientDto;
import com.garage.vehicle_service.dto.VehicleDto;
import com.garage.vehicle_service.entity.Vehicle;
import com.garage.vehicle_service.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String CLIENT_SERVICE_URL = "http://localhost:8081/api/clients";

    // Get all vehicles
    public List<VehicleDto> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get a vehicle by ID
    public VehicleDto getVehicleById(Long id) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        if (vehicleOpt.isPresent()) {
            return convertToDto(vehicleOpt.get());
        } else {
            throw new IllegalArgumentException("Vehicle not found with id: " + id);
        }
    }

    // Add a new vehicle
    public VehicleDto addVehicle(VehicleDto vehicleDto) {
        // Validate the owner ID by checking with Client Service
        if (!isOwnerValid(vehicleDto.getOwnerId())) {
            throw new IllegalArgumentException("Owner ID not found in Client Service");
        }

        // Proceed with saving the vehicle
        Vehicle vehicle = convertToEntity(vehicleDto);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToDto(savedVehicle);
    }

    // Update an existing vehicle
    public VehicleDto updateVehicle(Long id, VehicleDto vehicleDto) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            // Update fields as necessary
            vehicle.setVin(vehicleDto.getVin());
            vehicle.setRegistrationNumber(vehicleDto.getRegistrationNumber());
            vehicle.setBrand(vehicleDto.getBrand());
            vehicle.setModel(vehicleDto.getModel());
            vehicle.setYear(vehicleDto.getYear());
            vehicle.setColor(vehicleDto.getColor());
            vehicle.setMileage(vehicleDto.getMileage());
            vehicle.setFuelType(vehicleDto.getFuelType());
            vehicle.setPurchaseDate(vehicleDto.getPurchaseDate());
            vehicle.setOwnerId(vehicleDto.getOwnerId());
            vehicle.setStatus(vehicleDto.getStatus());

            Vehicle updatedVehicle = vehicleRepository.save(vehicle);
            return convertToDto(updatedVehicle);
        } else {
            throw new IllegalArgumentException("Vehicle not found with id: " + id);
        }
    }

    // Delete a vehicle
    public void deleteVehicle(Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Vehicle not found with id: " + id);
        }
    }

    // Validate the owner ID from the client service
    private boolean isOwnerValid(String ownerId) {
        String url = CLIENT_SERVICE_URL; // Fetch all clients from this endpoint

        try {
            // Fetch list of clients from Client Service
            List<ClientDto> clients = webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> Mono.error(new IllegalArgumentException("Error fetching clients from Client Service"))
                    )
                    .bodyToFlux(ClientDto.class) // Parse each client as ClientDto
                    .collectList()
                    .block(); // Block to wait for the response (since this is a synchronous operation)

            // Check if the given ownerId exists in the client list
            return clients != null && clients.stream()
                    .anyMatch(client -> client.getId().toString().equals(ownerId));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to validate owner ID: " + e.getMessage());
        }
    }

    // Convert Vehicle entity to DTO
    private VehicleDto convertToDto(Vehicle vehicle) {
        return VehicleDto.builder()
                .vin(vehicle.getVin())
                .registrationNumber(vehicle.getRegistrationNumber())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .mileage(vehicle.getMileage())
                .fuelType(vehicle.getFuelType())
                .purchaseDate(vehicle.getPurchaseDate())
                .ownerId(vehicle.getOwnerId())
                .status(vehicle.getStatus())
                .build();
    }

    // Convert Vehicle DTO to entity
    private Vehicle convertToEntity(VehicleDto dto) {
        return Vehicle.builder()
                .vin(dto.getVin())
                .registrationNumber(dto.getRegistrationNumber())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .year(dto.getYear())
                .color(dto.getColor())
                .mileage(dto.getMileage())
                .fuelType(dto.getFuelType())
                .purchaseDate(dto.getPurchaseDate())
                .ownerId(dto.getOwnerId())
                .status(dto.getStatus())
                .build();
    }
}
