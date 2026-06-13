package com.naveen.WorkForceMgmt.controller;

import com.naveen.WorkForceMgmt.dto.AuthResponse;
import com.naveen.WorkForceMgmt.dto.LoginRequest;
import com.naveen.WorkForceMgmt.dto.RegisterRequest;
import com.naveen.WorkForceMgmt.model.Employee;
import com.naveen.WorkForceMgmt.model.User;
import com.naveen.WorkForceMgmt.repository.EmployeeRepo;
import com.naveen.WorkForceMgmt.repository.UserRepository;
import com.naveen.WorkForceMgmt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserDetailsService userDetailsService;

  @Autowired private JwtService jwtService;

  @Autowired private EmployeeRepo employeeRepository; // to link to an employee

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

    final String jwtToken = jwtService.generateToken(userDetails);
    return ResponseEntity.ok(new AuthResponse(jwtToken));
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    // 1. Fetch employee
    Employee employee =
        employeeRepository
            .findById(request.getEmployeeId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
    // 2. Create User and hash password
    User user =
        User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword())) // <-- HASH IT!
            .role(request.getRole())
            .employee(employee)
            .build();
    userRepository.save(user);
    return ResponseEntity.ok("User registered successfully");
  }
}
