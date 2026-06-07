package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

  // Fetch all tasks belonging to a specific project
  List<Task> findByProjectId(Long projectId);

  // Fetch all tasks assigned to a specific employee
  List<Task> findByAssignedEmployeeId(Long employeeId);
}
