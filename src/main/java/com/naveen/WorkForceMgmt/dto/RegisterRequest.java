package com.naveen.WorkForceMgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
  private String password;

  @NotBlank(message = "Role is required")
  private String roleName;

  @NotNull(message = "Employee ID is required")
  private Long employeeId;

  /**
   * Explicit override so the password is never logged — e.g. by the audit-logging aspect, which
   * logs method arguments. Also blocks Lombok from generating a field-dumping toString()
   * if @Data/@ToString is ever added to this class later, since Lombok skips generation when a
   * toString() is already defined.
   */
  @Override
  public String toString() {
    return "RegisterRequest{username="
        + username
        + ", password=[PROTECTED], roleName="
        + roleName
        + ", employeeId="
        + employeeId
        + "}";
  }
}
