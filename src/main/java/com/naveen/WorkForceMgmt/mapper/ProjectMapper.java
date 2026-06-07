package com.naveen.WorkForceMgmt.mapper;

import com.naveen.WorkForceMgmt.dto.ProjectDTO;
import com.naveen.WorkForceMgmt.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

  ProjectDTO toDto(Project project);

  @Mapping(target = "tasks", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Project toEntity(ProjectDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "tasks", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateProjectFromDto(ProjectDTO dto, @MappingTarget Project project);
}
