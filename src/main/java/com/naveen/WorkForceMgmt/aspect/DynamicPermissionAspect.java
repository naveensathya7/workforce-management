package com.naveen.WorkForceMgmt.aspect;

import com.naveen.WorkForceMgmt.model.RoleMethodPermission;
import com.naveen.WorkForceMgmt.model.User;
import com.naveen.WorkForceMgmt.repository.RoleMethodPermissionRepository;
import java.util.Arrays;
import java.util.Optional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect that enforces dynamic, database-driven Role-Based Access Control (RBAC).
 *
 * <p>Instead of hardcoding @PreAuthorize("hasRole('ADMIN')") on every method, this Aspect
 * intercepts ALL @RestController method calls at runtime and checks the caller's permissions
 * against the "role_method_permissions" table in the database.
 *
 * <p>This means permissions can be changed in the database (or CSV file) without touching any Java
 * code or redeploying the application.
 */
@Aspect
@Component
public class DynamicPermissionAspect {

  @Autowired private RoleMethodPermissionRepository permissionRepo;

  /**
   * Pointcut: intercepts ALL methods inside any class annotated with @RestController. This runs
   * BEFORE the actual controller method is executed.
   */
  @Before("within(@org.springframework.web.bind.annotation.RestController *)")
  public void checkPermission(JoinPoint joinPoint) throws Throwable {

    // Step 1: Identify which controller and which method is being called via Reflection
    String controllerName = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();

    // Step 2: Get the current authentication state from Spring Security's context.
    // SecurityContextHolder holds the authentication object for the current request thread.
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Skip permission check for unauthenticated/anonymous requests (e.g. /login, /register).
    // Spring Security will already handle blocking truly unauthorized access via the filter chain.
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication.getPrincipal().equals("anonymousUser")) {
      return;
    }

    // Cast the principal to our custom User entity.
    // This works because UserDetailsServiceImpl.loadUserByUsername() returns a User object.
    User currentUser = (User) authentication.getPrincipal();
    Long roleId = currentUser.getRole().getId();

    // Step 3: Query the "role_method_permissions" table for this role + controller combination.
    // e.g. Find the row where role_id=2 AND controller='EmployeeController'
    Optional<RoleMethodPermission> permOpt =
        permissionRepo.findByRoleIdAndController(roleId, controllerName);

    // If no permission row exists for this role + controller, deny access entirely.
    if (permOpt.isEmpty()) {
      throw new AccessDeniedException(
          "Role has no permissions configured for controller: " + controllerName);
    }

    // Step 4: Check if the specific method being called is in the allowed methods list.
    // The "methods" column holds either "*" (allow all) or a CSV like "getEmployee,createEmployee".
    String allowedMethods = permOpt.get().getMethods();

    // "*" is a wildcard meaning the role can call any method in this controller
    boolean isAllowed =
        allowedMethods.equals("*") || Arrays.asList(allowedMethods.split(",")).contains(methodName);

    // If the specific method is not in the allowed list, throw a 403 Forbidden
    if (!isAllowed) {
      throw new AccessDeniedException(
          "Role is not permitted to call: " + methodName + " on " + controllerName);
    }
  }
}
