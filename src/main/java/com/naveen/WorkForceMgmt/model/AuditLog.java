package com.naveen.WorkForceMgmt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String action;

  @Column(columnDefinition = "TEXT")
  private String details;

  @Column(name = "performed_by")
  private String performedBy;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @Column(name = "execution_time_ms")
  private Long executionTimeMs;
}
