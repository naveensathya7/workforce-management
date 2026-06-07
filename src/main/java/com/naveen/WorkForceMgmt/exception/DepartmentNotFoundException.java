package com.naveen.WorkForceMgmt.exception;

public class DepartmentNotFoundException extends RuntimeException {

  public DepartmentNotFoundException(Long depId) {
    super("Department not found with id: " + depId);
  }
}
