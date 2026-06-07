package com.naveen.WorkForceMgmt.mapper;

import com.naveen.WorkForceMgmt.dto.DepartmentDTO;
import com.naveen.WorkForceMgmt.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

  DepartmentDTO toDto(Department department);

  @Mapping(target = "employees", ignore = true)
  Department toEntity(DepartmentDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "employees", ignore = true)
  void updateDepartmentFromDto(DepartmentDTO dto, @MappingTarget Department department);
}
