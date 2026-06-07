package com.naveen.WorkForceMgmt.dto;

import com.naveen.WorkForceMgmt.enums.TaskPriority;
import com.naveen.WorkForceMgmt.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {

  private Long id;

  @NotBlank(message = "Task title is required")
  private String title;

  private String description;

  @NotNull(message = "Task status is required")
  private TaskStatus status;

  @NotNull(message = "Task priority is required")
  private TaskPriority priority;

  private LocalDate dueDate;

  // IDs only — avoid exposing full nested entities
  @NotNull(message = "Assigned employee ID is required")
  private Long assignedEmployeeId;

  @NotNull(message = "Project ID is required")
  private Long projectId;
}
