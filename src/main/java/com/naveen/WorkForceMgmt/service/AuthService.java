package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.dto.AuthResponse;
import com.naveen.WorkForceMgmt.dto.ChangePasswordRequest;
import com.naveen.WorkForceMgmt.dto.LoginRequest;
import com.naveen.WorkForceMgmt.dto.RegisterRequest;
import com.naveen.WorkForceMgmt.dto.ResetPasswordRequest;
import com.naveen.WorkForceMgmt.model.Employee;
import com.naveen.WorkForceMgmt.model.User;
import com.naveen.WorkForceMgmt.repository.EmployeeRepo;
import com.naveen.WorkForceMgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;
  private final EmployeeRepo employeeRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    final String jwtToken = jwtService.generateToken(userDetails);
    return new AuthResponse(jwtToken);
  }

  @Transactional
  public void register(RegisterRequest request) {
    Employee employee =
        employeeRepository
            .findById(request.getEmployeeId())
            .orElseThrow(
                () ->
                    new RuntimeException("Employee not found with ID: " + request.getEmployeeId()));

    User user =
        User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
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
  }
}
