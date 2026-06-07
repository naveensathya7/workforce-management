package com.naveen.WorkForceMgmt.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import com.naveen.WorkForceMgmt.model.Employee;
import org.springframework.util.StringUtils;

import com.naveen.WorkForceMgmt.dto.EmployeeFilter;
import jakarta.persistence.criteria.Predicate;
public class EmployeeSpecification {

  public static Specification<Employee> filterBy(EmployeeFilter filter) {
    return (root,query,criteriaBuilder)->{
      List<Predicate> predicates = new ArrayList<>();

      if(StringUtils.hasText(filter.getQuery())){
        String searchPattern="%"+filter.getQuery().toLowerCase()+"%";
        Predicate globalSearchPredicate=criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),searchPattern),criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),searchPattern),criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),searchPattern),criteriaBuilder.like(criteriaBuilder.lower(root.get("designation")),searchPattern));
        predicates.add(globalSearchPredicate);
      }

      if(StringUtils.hasText(filter.getFirstName())){
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),"%"+filter.getFirstName().toLowerCase()+"%"));
      }

      if(StringUtils.hasText(filter.getLastName())){
        predicates.add(criteriaBuilder.equal(root.get("lastName"), filter.getLastName()));
      }

      if(StringUtils.hasText(filter.getEmail())){
        predicates.add(criteriaBuilder.equal(root.get("email"), filter.getEmail()));
      }

      if(StringUtils.hasText(filter.getPhone())){
        predicates.add(criteriaBuilder.equal(root.get("phone"), filter.getPhone()));
      }
      
      if(StringUtils.hasText(filter.getDesignation())){
        predicates.add(criteriaBuilder.equal(root.get("designation"), filter.getDesignation()));
      }

      if(filter.getDepartmentId() != null){
        predicates.add(criteriaBuilder.equal(root.get("department").get("id"), filter.getDepartmentId()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      
    };
  }
  
}
