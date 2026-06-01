package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.dto.EmployeeDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepo {

  List<EmployeeDTO> employeeList=new ArrayList<>();

  public void createEmployee(EmployeeDTO emp){
      employeeList.add(emp);
  }
    public void updateEmployee(int empId,EmployeeDTO emp){

        for(EmployeeDTO e:employeeList){
            if(e.getId()==empId){
                e.setName(emp.getName());
                e.setEmail(emp.getEmail());
                e.setDesignation(emp.getDesignation());
                e.setPhoneNumber(emp.getPhoneNumber());
                break;
            }
        }
    }
    public void deleteEmployee(int empId){
        for(EmployeeDTO e:employeeList){
            if(e.getId()==empId){
                employeeList.remove(e);
                break;
            }
        }
    }

    public EmployeeDTO getEmployee(int empId){
        for(EmployeeDTO e:employeeList){
            if(e.getId()==empId){
                return e;
            }
        }
        return null;
    }

    public List<EmployeeDTO> getAllEmployees(){
      return employeeList;
    }
}
