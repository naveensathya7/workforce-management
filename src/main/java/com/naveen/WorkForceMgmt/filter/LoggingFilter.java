package com.naveen.WorkForceMgmt.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {
  private static final String CORRELATION_HEADER="X-Correlation-ID";
  private static final String MDC_KEY="correlationId";

  @Override
  protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException,IOException{


    String correlationId=request.getHeader(CORRELATION_HEADER);
    if(correlationId==null || correlationId.trim().isEmpty()){
      correlationId=UUID.randomUUID().toString();
    }
    MDC.put(MDC_KEY,correlationId);

    response.setHeader(CORRELATION_HEADER,correlationId);

    long startTime=System.currentTimeMillis();

    try{
      String queryParams=request.getQueryString()!=null ? "?"+request.getQueryString():"";
      log.info("Incoming Request:[Method: {}] [URI: {}{}] [Client IP: {}]",request.getMethod(),request.getRequestURI(),queryParams,request.getRemoteAddr());

      filterChain.doFilter(request, response);

      long duration=System.currentTimeMillis()-startTime;
      log.info("Outgoing Response: [Status: {}] [Duration: {}]",response.getStatus(),duration);
    }finally{
      MDC.remove(MDC_KEY);
    }
  }
  
}
