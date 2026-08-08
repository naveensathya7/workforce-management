package com.naveen.WorkForceMgmt.filter;

import com.naveen.WorkForceMgmt.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  @Autowired private JwtService jwtService;

  @Autowired private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);

    try {
      username = jwtService.extractUsername(jwt);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(jwt, userDetails)) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (ExpiredJwtException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"JWT token has expired. Please log in again.\"}");
      return;
    } catch (JwtException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"Invalid JWT token.\"}");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
