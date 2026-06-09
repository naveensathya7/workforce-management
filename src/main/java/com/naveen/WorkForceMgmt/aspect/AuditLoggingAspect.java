package com.naveen.WorkForceMgmt.aspect;

import com.naveen.WorkForceMgmt.annotation.Auditable;
import com.naveen.WorkForceMgmt.model.AuditLog;
import com.naveen.WorkForceMgmt.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLoggingAspect {
  private final AuditLogRepository auditLogRepository;

  @Around("@annotation(auditable)")
  public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {

    long startTime = System.currentTimeMillis();
    String actionName = auditable.action();
    String methodArgs = Arrays.toString(joinPoint.getArgs());

    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    String username = "SYSTEM/UNKNOWN";
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();

      String userHeader = request.getHeader("X-user-Id");
      if (userHeader != null && !userHeader.isBlank()) {
        username = userHeader;
      } else {
        username = "IP: " + request.getRemoteAddr();
      }
    }

    Object result;

    try {
      result = joinPoint.proceed();
      long executionTime = System.currentTimeMillis() - startTime;
      AuditLog auditLog =
          AuditLog.builder()
              .action(actionName)
              .details(methodArgs)
              .performedBy(username)
              .timestamp(LocalDateTime.now())
              .executionTimeMs(executionTime)
              .build();
      log.info("Audit log: {}", auditLog);
      auditLogRepository.save(auditLog);

      return result;
    } catch (Throwable throwable) {
      log.error("Failed action: {} due to {}", actionName, throwable.getMessage());
      throw throwable;
    }
  }
}
