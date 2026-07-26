package com.naveen.WorkForceMgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

  @NotBlank(message = "New password is required")
  @Size(min = 8, max = 16, message = "New password should be between 8 and 16 characters")
  private String newPassword;
}
