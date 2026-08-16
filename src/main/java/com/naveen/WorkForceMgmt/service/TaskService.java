package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.annotation.Auditable;
import com.naveen.WorkForceMgmt.dto.TaskDTO;
import com.naveen.WorkForceMgmt.exception.EmployeeNotFoundException;
import com.naveen.WorkForceMgmt.exception.ProjectNotFoundException;
import com.naveen.WorkForceMgmt.exception.TaskNotFoundException;
import com.naveen.WorkForceMgmt.mapper.TaskMapper;
import com.naveen.WorkForceMgmt.model.Employee;
import com.naveen.WorkForceMgmt.model.Project;
import com.naveen.WorkForceMgmt.model.Task;
import com.naveen.WorkForceMgmt.repository.EmployeeRepo;
import com.naveen.WorkForceMgmt.repository.ProjectRepo;
import com.naveen.WorkForceMgmt.repository.TaskRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  @Autowired private TaskRepo taskRepo;

  @Autowired private ProjectRepo projectRepo;

  @Autowired private EmployeeRepo employeeRepo;

  @Autowired private TaskMapper taskMapper;

  public List<Task> getAllTasks() {
    return taskRepo.findAll();
  }

  public Task getTask(Long taskId) {
    return taskRepo.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
  }

  public List<Task> getTasksByProject(Long projectId) {
    return taskRepo.findByProjectId(projectId);
  }

  public List<Task> getTasksByEmployee(Long employeeId) {
    return taskRepo.findByAssignedEmployeeId(employeeId);
  }

  @Auditable(action = "CREATE_TASK")
  public void createTask(TaskDTO dto) {
    // Validate that both the employee and project actually exist
    Employee employee =
        employeeRepo
            .findById(dto.getAssignedEmployeeId())
            .orElseThrow(() -> new EmployeeNotFoundException(dto.getAssignedEmployeeId()));

    Project project =
        projectRepo
            .findById(dto.getProjectId())
            .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));

    Task task = taskMapper.toEntity(dto);
    task.setAssignedEmployee(employee);
    task.setProject(project);
    taskRepo.save(task);
  }

  @Auditable(action = "UPDATE_TASK")
  public void updateTask(Long taskId, TaskDTO dto) {
    Task existing = taskRepo.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));

    Employee employee =
        employeeRepo
            .findById(dto.getAssignedEmployeeId())
            .orElseThrow(() -> new EmployeeNotFoundException(dto.getAssignedEmployeeId()));

    Project project =
        projectRepo
            .findById(dto.getProjectId())
            .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));

    taskMapper.updateTaskFromDto(dto, existing);
    existing.setAssignedEmployee(employee);
    existing.setProject(project);
    taskRepo.save(existing);
  }

  @Auditable(action = "DELETE_TASK")
  public void deleteTask(Long taskId) {
    if (!taskRepo.existsById(taskId)) {
      throw new TaskNotFoundException(taskId);
    }
    taskRepo.deleteById(taskId);
  }
}
