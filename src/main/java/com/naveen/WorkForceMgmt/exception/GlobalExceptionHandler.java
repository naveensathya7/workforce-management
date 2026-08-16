package com.naveen.WorkForceMgmt.exception;

import com.naveen.WorkForceMgmt.dto.ErrorResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ControllerAdvice — makes this class a global handler that intercepts exceptions thrown from ANY
 * controller in the application.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * @ExceptionHandler(MethodArgumentNotValidException.class) Triggered when @Valid fails on
   * a @RequestBody. Collects every field-level validation error message and returns them all
   * together in a 400 Bad Request response.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
      MethodArgumentNotValidException ex) {

    // Extract all field error messages from the binding result
    List<String> errorMessages =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .toList();
    // Log field validation errors at WARN level
    log.warn("Validation failed: {}", errorMessages);

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            errorMessages,
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * @ExceptionHandler(EmployeeNotFoundException.class) Triggered when the service/repo cannot find
   * an employee by ID. Returns 404 Not Found.
   */
  @ExceptionHandler(EmployeeNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleEmployeeNotFound(EmployeeNotFoundException ex) {

    log.warn("Employee not found: {}", ex.getMessage());
    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            "Employee Not Found",
            List.of(ex.getMessage()),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * @ExceptionHandler(DuplicateEmployeeException.class) Triggered when trying to create an employee
   * with an ID that already exists. Returns 409 Conflict.
   */
  @ExceptionHandler(DuplicateEmployeeException.class)
  public ResponseEntity<ErrorResponseDTO> handleDuplicateEmployee(DuplicateEmployeeException ex) {

    log.warn("Duplicate employee attempt: {}", ex.getMessage());

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.CONFLICT.value(),
            "Duplicate Employee",
            List.of(ex.getMessage()),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DepartmentNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleDepartmentNotFound(DepartmentNotFoundException ex) {

    log.warn("Department not found: {}", ex.getMessage());

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            "Department Not Found",
            List.of(ex.getMessage()),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleProjectNotFound(ProjectNotFoundException ex) {

    log.warn("Project not found: {}", ex.getMessage());

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            "Project Not Found",
            List.of(ex.getMessage()),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleTaskNotFound(TaskNotFoundException ex) {

    log.warn("Task not found: {}", ex.getMessage());

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            "Task Not Found",
            List.of(ex.getMessage()),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * Triggered when authentication fails due to incorrect username or password. Returns 401
   * Unauthorized instead of 500 Internal Server Error.
   */
  @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
  public ResponseEntity<ErrorResponseDTO> handleBadCredentials(
      org.springframework.security.authentication.BadCredentialsException ex) {

    log.warn("Authentication failed: Invalid username or password");

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.UNAUTHORIZED.value(),
            "Authentication Failed",
            List.of("Invalid username or password"),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Triggered when user account is disabled/banned (is_enabled = false). Returns 401 Unauthorized.
   */
  @ExceptionHandler(org.springframework.security.authentication.DisabledException.class)
  public ResponseEntity<ErrorResponseDTO> handleDisabledAccount(
      org.springframework.security.authentication.DisabledException ex) {

    log.warn("Authentication failed: Account is disabled");

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.UNAUTHORIZED.value(),
            "Account Disabled",
            List.of("User account is disabled. Please contact administrator."),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  /** Triggered when a refresh token is missing, expired, or reused. Returns 401 Unauthorized. */
  @ExceptionHandler(InvalidRefreshTokenException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidRefreshToken(
      InvalidRefreshTokenException ex) {

    log.warn("Refresh token rejected: {}", ex.getMessage());

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.UNAUTHORIZED.value(),
            "Invalid Refresh Token",
            List.of(ex.getMessage()),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Triggered when an authenticated user is unauthorized to access the resource. Returns 403
   * Forbidden.
   */
  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  public ResponseEntity<ErrorResponseDTO> handleUnauthorizedAccess(
      org.springframework.security.access.AccessDeniedException ex) {

    log.warn("Authorization failed: Access denied");

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.FORBIDDEN.value(),
            "Access Denied",
            List.of(
                "You do not have permission to perform this action. Please contact administrator."),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  /**
   * Triggered when a request doesn't match any controller mapping (e.g. a typo'd URL or an extra
   * path segment). Returns 404 Not Found instead of falling through to the generic 500 handler.
   */
  @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleNoResourceFound(
      org.springframework.web.servlet.resource.NoResourceFoundException ex) {

    log.warn("No matching route: {}", ex.getMessage());

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            List.of("The requested URL does not match any endpoint."),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * Catch-all handler for any other unexpected runtime exceptions. Returns 500 Internal Server
   * Error.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {

    log.error("An unexpected error occurred: ", ex);

    ErrorResponseDTO error =
        new ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            List.of(ex.getMessage()),
            LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
