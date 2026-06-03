package com.naveen.WorkForceMgmt.service;


import com.naveen.WorkForceMgmt.exception.EmployeeNotFoundException;
import com.naveen.WorkForceMgmt.model.Department;
import com.naveen.WorkForceMgmt.repository.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepo departmentRepo;

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public Department getDepartment(Long departmentId) {
        return departmentRepo.findById(departmentId).orElseThrow(() ->
                new EmployeeNotFoundException(departmentId));
    }

    public void createNewDepartment(Department department) {
        departmentRepo.save(department);
    }

    public void updateDepartment(Long departmentId, Department department) {
        Department existingDepartment=departmentRepo.findById(departmentId).orElseThrow(() ->
                new EmployeeNotFoundException(departmentId));
        existingDepartment.setName(department.getName());
        existingDepartment.setDescription((department.getDescription()));
        departmentRepo.save(existingDepartment);
    }

    public void deleteDepartment(Long departmentId) {
        departmentRepo.deleteById(departmentId);
    }
}
