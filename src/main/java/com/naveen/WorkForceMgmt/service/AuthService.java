package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.dto.AuthResponse;
import com.naveen.WorkForceMgmt.dto.ChangePasswordRequest;
import com.naveen.WorkForceMgmt.dto.LoginRequest;
import com.naveen.WorkForceMgmt.dto.RefreshTokenRequest;
import com.naveen.WorkForceMgmt.dto.RegisterRequest;
import com.naveen.WorkForceMgmt.dto.ResetPasswordRequest;
import com.naveen.WorkForceMgmt.model.Employee;
import com.naveen.WorkForceMgmt.model.RefreshToken;
import com.naveen.WorkForceMgmt.model.Role;
import com.naveen.WorkForceMgmt.model.User;
import com.naveen.WorkForceMgmt.repository.EmployeeRepo;
import com.naveen.WorkForceMgmt.repository.RoleRepository;
import com.naveen.WorkForceMgmt.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final EmployeeRepo employeeRepository;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenService refreshTokenService;

  public AuthResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    final User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(
                () ->
                    new RuntimeException("User not found with username: " + request.getUsername()));
    final Map<String, Object> claims = Map.of("tv", user.getTokenVersion());
    final String jwtToken = jwtService.generateToken(claims, user);
    final RefreshToken refreshToken =
        refreshTokenService.createRefreshToken(user, request.getDeviceId());
    return new AuthResponse(jwtToken, refreshToken.getToken());
  }

  @Transactional
  public void register(RegisterRequest request) {
    Employee employee =
        employeeRepository
            .findById(request.getEmployeeId())
            .orElseThrow(
                () ->
                    new RuntimeException("Employee not found with ID: " + request.getEmployeeId()));

    Role role =
        roleRepository
            .findByName(request.getRoleName())
            .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRoleName()));

    User user =
        User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(role)
            .employee(employee)
            .build();

    userRepository.save(user);
  }

  @Transactional
  public void changePassword(String username, ChangePasswordRequest request) {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username));

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new BadCredentialsException("Invalid current password");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
    userRepository.incrementTokenVersion(user.getId());
    refreshTokenService.deleteByUser(user);
  }

  @Transactional
  public void logout(String refreshToken) {
    refreshTokenService.deleteByToken(refreshToken);
  }

  @Transactional
  public AuthResponse refreshToken(RefreshTokenRequest request) {
    RefreshToken refreshToken =
        refreshTokenService
            .findByToken(request.getRefreshToken())
            .orElseThrow(() -> new RuntimeException("No Refresh token details found"));
    if (refreshToken.isRevoked()) {
      throw new RuntimeException("Refresh token is revoked");
    }
    refreshTokenService.verifyExpiration(refreshToken);
    User user = refreshToken.getUser();
    Map<String, Object> claims = Map.of("tv", user.getTokenVersion());
    String jwtToken = jwtService.generateToken(claims, user);
    // 3. Rotate: Delete old refresh token & generate a brand-new Refresh Token
    RefreshToken newRefreshToken =
        refreshTokenService.createRefreshToken(user, refreshToken.getDeviceId());
    return new AuthResponse(jwtToken, newRefreshToken.getToken());
  }

  @Transactional
  public void resetPassword(String username, ResetPasswordRequest request) {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username));

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
    userRepository.incrementTokenVersion(user.getId());
    refreshTokenService.deleteByUser(user);
  }
}
