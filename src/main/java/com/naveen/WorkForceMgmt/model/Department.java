package com.naveen.WorkForceMgmt.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "departments")
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "dep_id")
  private Long id;

  private String name;
  private String description;

  @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
  private List<Employee> employees;
}
