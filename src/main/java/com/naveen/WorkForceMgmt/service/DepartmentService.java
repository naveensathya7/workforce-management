package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.annotation.Auditable;
import com.naveen.WorkForceMgmt.dto.DepartmentDTO;
import com.naveen.WorkForceMgmt.exception.DepartmentNotFoundException;
import com.naveen.WorkForceMgmt.mapper.DepartmentMapper;
import com.naveen.WorkForceMgmt.model.Department;
import com.naveen.WorkForceMgmt.repository.DepartmentRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

  @Autowired private DepartmentRepo departmentRepo;

  @Autowired private DepartmentMapper departmentMapper;

  public List<Department> getAllDepartments() {
    return departmentRepo.findAll();
  }

  public Department getDepartment(Long departmentId) {
    return departmentRepo
        .findById(departmentId)
        .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
  }

  @Auditable(action = "CREATE_DEPARTMENT")
  public void createNewDepartment(DepartmentDTO dto) {
    Department department = departmentMapper.toEntity(dto);
    departmentRepo.save(department);
  }

  @Auditable(action = "UPDATE_DEPARTMENT")
  public void updateDepartment(Long departmentId, DepartmentDTO dto) {
    Department existingDepartment =
        departmentRepo
            .findById(departmentId)
            .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
    departmentMapper.updateDepartmentFromDto(dto, existingDepartment);
    departmentRepo.save(existingDepartment);
  }

  @Auditable(action = "DELETE_DEPARTMENT")
  public void deleteDepartment(Long departmentId) {
    if (!departmentRepo.existsById(departmentId)) {
      throw new DepartmentNotFoundException(departmentId);
    }
    departmentRepo.deleteById(departmentId);
  }
}
