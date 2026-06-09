package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {}
