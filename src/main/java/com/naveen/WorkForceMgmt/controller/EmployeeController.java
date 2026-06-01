package com.naveen.WorkForceMgmt.controller;

import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import com.naveen.WorkForceMgmt.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("employee")
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;

    @GetMapping("allEmployees")
    public List<EmployeeDTO> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("getEmployee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable("employeeId") int empId){
        return employeeService.getEmployee(empId);
    }

    @PostMapping("createEmployee")
    public String createEmployee(@RequestBody EmployeeDTO emp){
        employeeService.createEmployee(emp);
        return "Employee created";
    }

    @PostMapping("updateEmployee/{employeeId}")
    public String createEmployee(@PathVariable("employeeId") int empId,@RequestBody EmployeeDTO emp){
        employeeService.updateEmployee(empId,emp);
        return "Employee Updated";
    }

    @DeleteMapping("deleteEmployee/{employeeId}")
        public String deleteEmployee(@PathVariable("employeeId") int empId) {
        employeeService.deleteEmployee(empId);
        return "Employee Deleted";
    }

}
