package com.naveen.WorkForceMgmt.mapper;

import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import com.naveen.WorkForceMgmt.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  @Mapping(source = "department.id", target = "departmentId")
  EmployeeDTO tDto(Employee employee);

  @Mapping(target = "department", ignore = true)
  Employee toEntity(EmployeeDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "department", ignore = true)
  void updateEmployeeFromDto(EmployeeDTO dto, @MappingTarget Employee employee);
}
