package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.dto.ProjectDTO;
import com.naveen.WorkForceMgmt.exception.ProjectNotFoundException;
import com.naveen.WorkForceMgmt.model.Project;
import com.naveen.WorkForceMgmt.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public Project getProject(Long projectId) {
        return projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    public void createProject(ProjectDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus(dto.getStatus());
        projectRepo.save(project);
    }

    public void updateProject(Long projectId, ProjectDTO dto) {
        Project existing = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setStatus(dto.getStatus());
        projectRepo.save(existing);
    }

    public void deleteProject(Long projectId) {
        if (!projectRepo.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
        projectRepo.deleteById(projectId);
    }
}
