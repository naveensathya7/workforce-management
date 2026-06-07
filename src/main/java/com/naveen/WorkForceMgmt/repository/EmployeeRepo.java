package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import com.naveen.WorkForceMgmt.exception.DuplicateEmployeeException;
import com.naveen.WorkForceMgmt.exception.EmployeeNotFoundException;
import com.naveen.WorkForceMgmt.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long>,JpaSpecificationExecutor<Employee> {

//    List<EmployeeDTO> employeeList = new ArrayList<>();
//
//    public void createEmployee(EmployeeDTO emp) {
//        // Check for duplicate ID before adding
//        boolean exists = employeeList.stream()
//                .anyMatch(e -> e.getId() == emp.getId());
//        if (exists) {
//            throw new DuplicateEmployeeException(emp.getId());
//        }
//        employeeList.add(emp);
//    }
//
//    public void updateEmployee(int empId, EmployeeDTO emp) {
//        for (EmployeeDTO e : employeeList) {
//            if (e.getId() == empId) {
//                e.setName(emp.getName());
//                e.setEmail(emp.getEmail());
//                e.setDesignation(emp.getDesignation());
//                e.setPhoneNumber(emp.getPhoneNumber());
//                return;
//            }
//        }
//        // If loop completes without finding the employee, throw exception
//        throw new EmployeeNotFoundException(empId);
//    }
//
//    public void deleteEmployee(int empId) {
//        for (EmployeeDTO e : employeeList) {
//            if (e.getId() == empId) {
//                employeeList.remove(e);
//                return;
//            }
//        }
//        // If loop completes without finding the employee, throw exception
//        throw new EmployeeNotFoundException(empId);
//    }
//
//    public EmployeeDTO getEmployee(int empId) {
//        return employeeList.stream()
//                .filter(e -> e.getId() == empId)
//                .findFirst()
//                .orElseThrow(() -> new EmployeeNotFoundException(empId));
//    }
//
//    public List<EmployeeDTO> getAllEmployees() {
//        return employeeList;
//    }
}

