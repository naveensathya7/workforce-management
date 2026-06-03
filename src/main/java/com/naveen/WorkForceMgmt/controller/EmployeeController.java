package com.naveen.WorkForceMgmt.controller;

import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import com.naveen.WorkForceMgmt.model.Employee;
import com.naveen.WorkForceMgmt.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployee(employeeId));
    }

    /**
     * @Valid — triggers Bean Validation on the incoming @RequestBody.
     * If any @NotBlank / @Email / @Pattern constraint fails, Spring throws
     * MethodArgumentNotValidException, which our GlobalExceptionHandler catches.
     */
    @PostMapping
    public ResponseEntity<String> createEmployee(@Valid @RequestBody Employee emp) {
        employeeService.createEmployee(emp);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody Employee emp) {
        employeeService.updateEmployee(employeeId, emp);
        return ResponseEntity.ok("Employee updated successfully");
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}

