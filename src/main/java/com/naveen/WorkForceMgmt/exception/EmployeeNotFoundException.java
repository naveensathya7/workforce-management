package com.naveen.WorkForceMgmt.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long empId) {
        super("Employee not found with id: " + empId);
    }
}
