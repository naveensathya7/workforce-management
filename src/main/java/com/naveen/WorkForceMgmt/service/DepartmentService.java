package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.dto.DepartmentDTO;
import com.naveen.WorkForceMgmt.exception.DepartmentNotFoundException;
import com.naveen.WorkForceMgmt.model.Department;
import com.naveen.WorkForceMgmt.repository.DepartmentRepo;
import com.naveen.WorkForceMgmt.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public Department getDepartment(Long departmentId) {
        return departmentRepo.findById(departmentId).orElseThrow(() ->
                new DepartmentNotFoundException(departmentId));
    }

    public void createNewDepartment(DepartmentDTO dto) {
        Department department = departmentMapper.toEntity(dto);
        departmentRepo.save(department);
    }

    public void updateDepartment(Long departmentId, DepartmentDTO dto) {
        Department existingDepartment = departmentRepo.findById(departmentId).orElseThrow(() ->
                new DepartmentNotFoundException(departmentId));
        departmentMapper.updateDepartmentFromDto(dto, existingDepartment);
        departmentRepo.save(existingDepartment);
    }

    public void deleteDepartment(Long departmentId) {
        if (!departmentRepo.existsById(departmentId)) {
            throw new DepartmentNotFoundException(departmentId);
        }
        departmentRepo.deleteById(departmentId);
    }
}
