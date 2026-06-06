package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import com.naveen.WorkForceMgmt.exception.DepartmentNotFoundException;
import com.naveen.WorkForceMgmt.exception.EmployeeNotFoundException;
import com.naveen.WorkForceMgmt.model.Department;
import com.naveen.WorkForceMgmt.model.Employee;
import com.naveen.WorkForceMgmt.repository.DepartmentRepo;
import com.naveen.WorkForceMgmt.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public Employee getEmployee(Long empId) {
        return employeeRepo.findById(empId).orElseThrow(() ->
                new EmployeeNotFoundException(empId));
    }

    public void createEmployee(EmployeeDTO dto) {
        Department department = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(dto.getDepartmentId()));

        Employee employee = new Employee();
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDesignation(dto.getDesignation());
        employee.setDepartment(department);

        employeeRepo.save(employee);
    }

    public void updateEmployee(Long empId, EmployeeDTO dto) {
        Employee employee = employeeRepo.findById(empId).orElseThrow(() ->
                new EmployeeNotFoundException(empId));

        Department department = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(dto.getDepartmentId()));

        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDesignation(dto.getDesignation());
        employee.setDepartment(department);

        employeeRepo.save(employee);
    }

    public void deleteEmployee(Long empId){
        employeeRepo.deleteById(empId);
    }
}
