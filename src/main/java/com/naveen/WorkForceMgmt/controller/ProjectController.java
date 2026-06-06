package com.naveen.WorkForceMgmt.controller;

import com.naveen.WorkForceMgmt.dto.ProjectDTO;
import com.naveen.WorkForceMgmt.model.Project;
import com.naveen.WorkForceMgmt.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @PostMapping
    public ResponseEntity<String> createProject(@Valid @RequestBody ProjectDTO dto) {
        projectService.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Project created successfully");
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<String> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectDTO dto) {
        projectService.updateProject(projectId, dto);
        return ResponseEntity.ok("Project updated successfully");
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok("Project deleted successfully");
    }
}
