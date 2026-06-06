package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.dto.DepartmentDTO;
import com.naveen.WorkForceMgmt.exception.DepartmentNotFoundException;
import com.naveen.WorkForceMgmt.model.Department;
import com.naveen.WorkForceMgmt.repository.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public Department getDepartment(Long departmentId) {
        return departmentRepo.findById(departmentId).orElseThrow(() ->
                new DepartmentNotFoundException(departmentId));
    }

    public void createNewDepartment(DepartmentDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        departmentRepo.save(department);
    }

    public void updateDepartment(Long departmentId, DepartmentDTO dto) {
        Department existingDepartment = departmentRepo.findById(departmentId).orElseThrow(() ->
                new DepartmentNotFoundException(departmentId));
        existingDepartment.setName(dto.getName());
        existingDepartment.setDescription(dto.getDescription());
        departmentRepo.save(existingDepartment);
    }

    public void deleteDepartment(Long departmentId) {
        if (!departmentRepo.existsById(departmentId)) {
            throw new DepartmentNotFoundException(departmentId);
        }
        departmentRepo.deleteById(departmentId);
    }
}
