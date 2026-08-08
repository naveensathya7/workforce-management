package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.model.RoleMethodPermission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMethodPermissionRepository extends JpaRepository<RoleMethodPermission, Long> {

  @Query(
      "SELECT p FROM RoleMethodPermission p WHERE p.role.id = :roleId AND p.controller = :controller")
  Optional<RoleMethodPermission> findByRoleIdAndController(
      @Param("roleId") Long roleId, @Param("controller") String controller);

  void deleteByRoleIdAndController(Long roleId, String controller);
}
