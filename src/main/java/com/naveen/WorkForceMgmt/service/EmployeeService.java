package com.naveen.WorkForceMgmt.service;


import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import com.naveen.WorkForceMgmt.exception.EmployeeNotFoundException;
import com.naveen.WorkForceMgmt.model.Employee;
import com.naveen.WorkForceMgmt.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {


    @Autowired
    private EmployeeRepo employeeRepo;

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public Employee getEmployee(Long empId) {
        return employeeRepo.findById(empId).orElseThrow(() ->
                new EmployeeNotFoundException(empId));
    }

    public void createEmployee(Employee emp) {
        employeeRepo.save(emp);
    }

    public void updateEmployee(Long empId, Employee emp) {
        Employee employee=employeeRepo.findById(empId).orElseThrow(() ->
                new EmployeeNotFoundException(empId));
        employee.setDesignation(emp.getDesignation());
        employee.setPhoneNumber(emp.getPhoneNumber());
//        employee.setEmployeeCode(emp.getEmployeeCode());
        employee.setFirstName(emp.getFirstName());
        employee.setLastName(emp.getLastName());
        employee.setEmail(emp.getEmail());
        employee.setDepartment(emp.getDepartment());
        employeeRepo.save(employee);
    }

    public void deleteEmployee(Long empId){
        employeeRepo.deleteById(empId);
    }
}
