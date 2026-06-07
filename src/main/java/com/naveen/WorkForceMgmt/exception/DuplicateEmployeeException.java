package com.naveen.WorkForceMgmt.exception;

public class DuplicateEmployeeException extends RuntimeException {

  public DuplicateEmployeeException(int empId) {
    super("Employee already exists with id: " + empId);
  }
}
