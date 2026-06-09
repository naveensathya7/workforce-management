package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.annotation.Auditable;
import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import com.naveen.WorkForceMgmt.dto.EmployeeFilter;
import com.naveen.WorkForceMgmt.exception.DepartmentNotFoundException;
import com.naveen.WorkForceMgmt.exception.EmployeeNotFoundException;
import com.naveen.WorkForceMgmt.mapper.EmployeeMapper;
import com.naveen.WorkForceMgmt.model.Department;
import com.naveen.WorkForceMgmt.model.Employee;
import com.naveen.WorkForceMgmt.repository.DepartmentRepo;
import com.naveen.WorkForceMgmt.repository.EmployeeRepo;
import com.naveen.WorkForceMgmt.specification.EmployeeSpecification;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  @Autowired private EmployeeRepo employeeRepo;

  @Autowired private DepartmentRepo departmentRepo;

  @Autowired private EmployeeMapper employeeMapper;

  public List<Employee> getAllEmployees() {
    return employeeRepo.findAll();
  }

  public Employee getEmployee(Long empId) {
    return employeeRepo.findById(empId).orElseThrow(() -> new EmployeeNotFoundException(empId));
  }

  @Auditable(action = "CREATE_EMPLOYEE")
  public void createEmployee(EmployeeDTO dto) {
    Department department =
        departmentRepo
            .findById(dto.getDepartmentId())
            .orElseThrow(() -> new DepartmentNotFoundException(dto.getDepartmentId()));

    Employee employee = employeeMapper.toEntity(dto);
    employee.setDepartment(department);

    employeeRepo.save(employee);
  }

  public void updateEmployee(Long empId, EmployeeDTO dto) {
    Employee employee =
        employeeRepo.findById(empId).orElseThrow(() -> new EmployeeNotFoundException(empId));

    Department department =
        departmentRepo
            .findById(dto.getDepartmentId())
            .orElseThrow(() -> new DepartmentNotFoundException(dto.getDepartmentId()));

    employeeMapper.updateEmployeeFromDto(dto, employee);
    employee.setDepartment(department);

    employeeRepo.save(employee);
  }

  public void deleteEmployee(Long empId) {
    employeeRepo.deleteById(empId);
  }

  public Page<Employee> getEmployeesPage(EmployeeFilter filter, Pageable page) {
    Specification<Employee> spec = EmployeeSpecification.filterBy(filter);
    return employeeRepo.findAll(spec, page);
  }
}
