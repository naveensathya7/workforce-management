package com.naveen.WorkForceMgmt.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(Long projectId) {
        super("Project not found with id: " + projectId);
    }
}
