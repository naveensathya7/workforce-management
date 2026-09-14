package com.naveen.WorkForceMgmt.exception;

public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException(String roleName) {
    super("Role not found: " + roleName);
  }
}
