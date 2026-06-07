package com.naveen.WorkForceMgmt.controller;

import com.naveen.WorkForceMgmt.dto.TaskDTO;
import com.naveen.WorkForceMgmt.model.Task;
import com.naveen.WorkForceMgmt.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired private TaskService taskService;

  @GetMapping
  public ResponseEntity<List<Task>> getAllTasks() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
    return ResponseEntity.ok(taskService.getTask(taskId));
  }

  // GET /tasks?projectId=1
  @GetMapping(params = "projectId")
  public ResponseEntity<List<Task>> getTasksByProject(@RequestParam Long projectId) {
    return ResponseEntity.ok(taskService.getTasksByProject(projectId));
  }

  // GET /tasks?employeeId=1
  @GetMapping(params = "employeeId")
  public ResponseEntity<List<Task>> getTasksByEmployee(@RequestParam Long employeeId) {
    return ResponseEntity.ok(taskService.getTasksByEmployee(employeeId));
  }

  @PostMapping
  public ResponseEntity<String> createTask(@Valid @RequestBody TaskDTO dto) {
    taskService.createTask(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully");
  }

  @PutMapping("/{taskId}")
  public ResponseEntity<String> updateTask(
      @PathVariable Long taskId, @Valid @RequestBody TaskDTO dto) {
    taskService.updateTask(taskId, dto);
    return ResponseEntity.ok("Task updated successfully");
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
    taskService.deleteTask(taskId);
    return ResponseEntity.ok("Task deleted successfully");
  }
}
