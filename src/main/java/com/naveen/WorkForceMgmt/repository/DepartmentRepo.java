package com.naveen.WorkForceMgmt.repository;


import com.naveen.WorkForceMgmt.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {
}
