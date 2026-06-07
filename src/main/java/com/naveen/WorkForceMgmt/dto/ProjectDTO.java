package com.naveen.WorkForceMgmt.dto;

import com.naveen.WorkForceMgmt.enums.ProjectStatus;
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
public class ProjectDTO {

  private Long id;

  @NotBlank(message = "Project name is required")
  private String name;

  private String description;

  @NotNull(message = "Start date is required")
  private LocalDate startDate;

  private LocalDate endDate;

  @NotNull(message = "Project status is required")
  private ProjectStatus status;
}
