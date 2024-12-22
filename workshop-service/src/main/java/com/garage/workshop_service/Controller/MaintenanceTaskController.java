package com.garage.workshop_service.Controller;

import com.garage.workshop_service.dto.MaintenanceTaskDto;
import com.garage.workshop_service.service.MaintenanceTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-tasks")
public class MaintenanceTaskController {

    @Autowired
    private MaintenanceTaskService taskService;

    // Create a new maintenance task
    @PostMapping
    public ResponseEntity<MaintenanceTaskDto> createTask(@RequestBody MaintenanceTaskDto taskDto) {
        MaintenanceTaskDto createdTask = taskService.createTask(taskDto);
        return ResponseEntity.ok(createdTask);
    }

    // Get all maintenance tasks
    @GetMapping
    public ResponseEntity<List<MaintenanceTaskDto>> getAllTasks() {
        List<MaintenanceTaskDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // Get a maintenance task by ID
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceTaskDto> getTaskById(@PathVariable Long id) {
        MaintenanceTaskDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    // Get maintenance tasks by date
    @GetMapping("/by-date")
    public ResponseEntity<List<MaintenanceTaskDto>> getTasksByDate(@RequestParam String date) {
        List<MaintenanceTaskDto> tasks = taskService.getTasksByDate(date);
        return ResponseEntity.ok(tasks);
    }

    // Update a maintenance task
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceTaskDto> updateTask(
            @PathVariable Long id,
            @RequestBody MaintenanceTaskDto updatedTaskDto) {
        MaintenanceTaskDto updatedTask = taskService.updateTask(id, updatedTaskDto);
        return ResponseEntity.ok(updatedTask);
    }

    // Delete a maintenance task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
