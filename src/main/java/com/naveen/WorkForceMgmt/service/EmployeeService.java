package com.naveen.WorkForceMgmt.service;


import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import com.naveen.WorkForceMgmt.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {


    @Autowired
    EmployeeRepo employeeRepo;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepo.getAllEmployees();
    }

    public EmployeeDTO getEmployee(int empId) {
        return employeeRepo.getEmployee(empId);
    }

    public void createEmployee(EmployeeDTO emp) {
        employeeRepo.createEmployee(emp);
    }

    public void updateEmployee(int empId, EmployeeDTO emp) {
        employeeRepo.updateEmployee(empId,emp);
    }

    public void deleteEmployee(int empId){
        employeeRepo.deleteEmployee(empId);
    }
}
