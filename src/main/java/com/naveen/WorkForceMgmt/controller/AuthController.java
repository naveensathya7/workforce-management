package com.naveen.WorkForceMgmt.controller;

import com.naveen.WorkForceMgmt.dto.AuthResponse;
import com.naveen.WorkForceMgmt.dto.ChangePasswordRequest;
import com.naveen.WorkForceMgmt.dto.LoginRequest;
import com.naveen.WorkForceMgmt.dto.RefreshTokenRequest;
import com.naveen.WorkForceMgmt.dto.RegisterRequest;
import com.naveen.WorkForceMgmt.dto.ResetPasswordRequest;
import com.naveen.WorkForceMgmt.service.AuthService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
    authService.register(request);
    return ResponseEntity.ok("User registered successfully");
  }

  /**
   * Users can change their OWN password. Username is extracted securely from the authenticated
   * Principal (JWT security context).
   */
  @PutMapping("/change-password")
  public ResponseEntity<String> changePassword(
      Principal principal, @Valid @RequestBody ChangePasswordRequest request) {
    authService.changePassword(principal.getName(), request);
    return ResponseEntity.ok("Password changed successfully");
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest request) {
    authService.logout(request.getRefreshToken());
    return ResponseEntity.ok("Logged out successfully");
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<AuthResponse> refreshToken(
      @Valid @RequestBody RefreshTokenRequest request) {
    return ResponseEntity.ok(authService.refreshToken(request));
  }

  /** ADMIN users can reset the password for any given user. */
  @PutMapping("/reset-password/{username}")
  public ResponseEntity<String> resetPassword(
      @PathVariable String username, @Valid @RequestBody ResetPasswordRequest request) {
    authService.resetPassword(username, request);
    return ResponseEntity.ok("Password reset successfully");
  }
}
