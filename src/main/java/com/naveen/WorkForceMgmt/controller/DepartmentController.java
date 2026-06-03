package com.naveen.WorkForceMgmt.controller;


import com.naveen.WorkForceMgmt.model.Department;
import com.naveen.WorkForceMgmt.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<Department> getDepartment(@PathVariable Long departmentId){
        return ResponseEntity.ok(departmentService.getDepartment(departmentId));
    }

    @PostMapping
    public ResponseEntity<String> createNewDepartment(@RequestBody Department department){
        departmentService.createNewDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body("Department created successfully");
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<String> updateDepartment(@PathVariable Long departmentId,@RequestBody Department department){
        departmentService.updateDepartment(departmentId,department);
        return ResponseEntity.ok("Department updated successfully");
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long departmentId){
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok("Department deleted successfully");
    }




}
