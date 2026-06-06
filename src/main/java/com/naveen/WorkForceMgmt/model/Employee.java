package com.naveen.WorkForceMgmt.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="employees")
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="employee_id")
    private Long id;

    @Column(unique = true)
    private String employeeCode;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="department_id",referencedColumnName = "dep_id")
    private Department department;
}
