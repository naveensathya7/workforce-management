package com.naveen.WorkForceMgmt.controller;

import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
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
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable int employeeId) {
        return ResponseEntity.ok(employeeService.getEmployee(employeeId));
    }

    /**
     * @Valid — triggers Bean Validation on the incoming @RequestBody.
     * If any @NotBlank / @Email / @Pattern constraint fails, Spring throws
     * MethodArgumentNotValidException, which our GlobalExceptionHandler catches.
     */
    @PostMapping
    public ResponseEntity<String> createEmployee(@Valid @RequestBody EmployeeDTO emp) {
        employeeService.createEmployee(emp);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updateEmployee(
            @PathVariable int employeeId,
            @Valid @RequestBody EmployeeDTO emp) {
        employeeService.updateEmployee(employeeId, emp);
        return ResponseEntity.ok("Employee updated successfully");
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}

