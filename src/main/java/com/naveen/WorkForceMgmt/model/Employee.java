package com.naveen.WorkForceMgmt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employees")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "employee_id")
  private Long id;

  @Column(unique = true)
  private String employeeCode;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  private String designation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id", referencedColumnName = "dep_id")
  @JsonIgnore
  private Department department;
}
