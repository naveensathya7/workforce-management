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
