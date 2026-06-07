package com.naveen.WorkForceMgmt.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {

  private int status;
  private String error;
  private List<String> messages;
  private LocalDateTime timestamp;
}
