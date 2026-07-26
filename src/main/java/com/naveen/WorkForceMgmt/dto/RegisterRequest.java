package com.naveen.WorkForceMgmt.dto;

import com.naveen.WorkForceMgmt.enums.Role;
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

  @NotNull(message = "Role is required")
  private Role role;

  @NotNull(message = "Employee ID is required")
  private Long employeeId;
}
