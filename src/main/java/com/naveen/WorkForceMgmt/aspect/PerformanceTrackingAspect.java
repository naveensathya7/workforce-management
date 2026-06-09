package com.naveen.WorkForceMgmt.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceTrackingAspect {

  @Around("@annotation(com.naveen.WorkForceMgmt.annotation.TrackTime)")
  public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    try {
      return joinPoint.proceed();

    } finally {
      Long endTime = System.currentTimeMillis();
      long executionTime = endTime - startTime;
      log.info(
          "Method {} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
    }
  }
}
