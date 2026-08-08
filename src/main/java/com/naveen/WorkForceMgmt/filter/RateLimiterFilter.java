package com.naveen.WorkForceMgmt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

  @Value("${rate.limiter.max-attempts:5}")
  private int maxAttempts;

  @Value("${rate.limiter.time-window-ms:60000}")
  private long timeWindowMs;

  private final Map<String, List<Long>> attemptsMap = new ConcurrentHashMap<>();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // Apply rate limiting to ALL POST requests
    if ("POST".equalsIgnoreCase(request.getMethod())) {
      String clientIp = getClientIp(request);
      String key = clientIp + ":" + request.getRequestURI();
      long currentTime = System.currentTimeMillis();

      List<Long> timeStamps = attemptsMap.computeIfAbsent(key, k -> new ArrayList<>());

      synchronized (timeStamps) {
        timeStamps.removeIf(timeStamp -> (currentTime - timeStamp) > timeWindowMs);

        if (timeStamps.size() >= maxAttempts) {
          response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
          response.setContentType("application/json");
          response
              .getWriter()
              .write(
                  "{\"error\": \"Too many requests to this endpoint. Please try again in 1 minute.\"}");
          return;
        }
        timeStamps.add(currentTime);
      }
    }
    filterChain.doFilter(request, response);
  }

  private String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");

    if (xForwardedFor != null && !xForwardedFor.isBlank()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
