package com.naveen.WorkForceMgmt.exception;

import com.naveen.WorkForceMgmt.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ControllerAdvice — makes this class a global handler that intercepts
 * exceptions thrown from ANY controller in the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @ExceptionHandler(MethodArgumentNotValidException.class)
     * Triggered when @Valid fails on a @RequestBody.
     * Collects every field-level validation error message and returns
     * them all together in a 400 Bad Request response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Extract all field error messages from the binding result
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errorMessages,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * @ExceptionHandler(EmployeeNotFoundException.class)
     * Triggered when the service/repo cannot find an employee by ID.
     * Returns 404 Not Found.
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmployeeNotFound(
            EmployeeNotFoundException ex) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Employee Not Found",
                List.of(ex.getMessage()),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * @ExceptionHandler(DuplicateEmployeeException.class)
     * Triggered when trying to create an employee with an ID that already exists.
     * Returns 409 Conflict.
     */
    @ExceptionHandler(DuplicateEmployeeException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateEmployee(
            DuplicateEmployeeException ex) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Duplicate Employee",
                List.of(ex.getMessage()),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Catch-all handler for any other unexpected runtime exceptions.
     * Returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                List.of(ex.getMessage()),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
