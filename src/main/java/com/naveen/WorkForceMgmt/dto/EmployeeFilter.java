package com.naveen.WorkForceMgmt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeFilter {

  private String query;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String designation;
  private Long departmentId;
}
