package com.naveen.WorkForceMgmt.mapper;

import com.naveen.WorkForceMgmt.dto.TaskDTO;
import com.naveen.WorkForceMgmt.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  @Mapping(source = "assignedEmployee.id", target = "assignedEmployeeId")
  @Mapping(source = "project.id", target = "projectId")
  TaskDTO toDto(Task task);

  @Mapping(target = "assignedEmployee", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Task toEntity(TaskDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "assignedEmployee", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateTaskFromDto(TaskDTO dto, @MappingTarget Task task);
}
