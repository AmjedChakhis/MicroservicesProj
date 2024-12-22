package com.garage.workshop_service.repository;



import com.garage.workshop_service.entity.MaintenanceTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long> {
    List<MaintenanceTask> findByDate(String date);
}

