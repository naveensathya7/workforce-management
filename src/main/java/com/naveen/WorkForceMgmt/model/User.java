package com.naveen.WorkForceMgmt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User entity that maps to the "users" table in the database.
 *
 * <p>Implements {@link UserDetails} — Spring Security's contract that tells the framework "this
 * object IS the authenticated principal". Spring Security calls these 7 methods during
 * authentication and authorization to decide: who are you? what can you do? is your account valid?
 *
 * <p>Linked to {@link Employee} via OneToOne — an Employee record represents the HR/business data,
 * while this User record represents login credentials and system access. Not every employee needs a
 * user account (e.g., contractors stored in employees but with no system login).
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  /** The login identifier — stored in the "username" column in DB. */
  @Column(name = "username", nullable = false, unique = true)
  private String username;

  /** BCrypt-hashed password — NEVER store plain text. */
  @Column(name = "password", nullable = false)
  private String password;

  /** Role drives what endpoints this user can access. */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  /**
   * The Employee HR record this login account belongs to.
   *
   * <p>Separation of concerns: - Employee = business/HR data (name, designation, department) - User
   * = security data (username, password, role)
   *
   * <p>Not every employee needs a user account. This link is created only when an employee is
   * granted system access by an ADMIN.
   *
   * <p>@JsonIgnore prevents the full Employee object from being serialized in responses that return
   * the User object (e.g., /me endpoint).
   */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", unique = true)
  @JsonIgnore
  private Employee employee;

  /**
   * Soft-delete / ban flag. When false, Spring Security rejects login with DisabledException — no
   * need to touch authentication logic elsewhere.
   */
  @Column(name = "is_enabled", nullable = false)
  @Builder.Default
  private boolean enabled = true;

  /**
   * Useful for password-expiry policies. Return false → Spring Security throws
   * CredentialsExpiredException at login time.
   */
  @Column(name = "is_credentials_non_expired", nullable = false)
  @Builder.Default
  private boolean credentialsNonExpired = true;

  /**
   * Account lock flag (e.g., after too many failed logins). Return false → Spring Security throws
   * LockedException at login time.
   */
  @Column(name = "is_account_non_locked", nullable = false)
  @Builder.Default
  private boolean accountNonLocked = true;

  /**
   * Account expiry flag (e.g., trial accounts). Return false → Spring Security throws
   * AccountExpiredException at login time.
   */
  @Column(name = "is_account_non_expired", nullable = false)
  @Builder.Default
  private boolean accountNonExpired = true;

  /** Security version counter used for token invalidation */
  @Column(name = "token_version", nullable = false)
  @Builder.Default
  private int tokenVersion = 0;

  // ─────────────────────────────────────────────────────────────
  // UserDetails contract — 7 methods Spring Security will call
  // ─────────────────────────────────────────────────────────────

  /**
   * METHOD 1: getAuthorities()
   *
   * <p>Returns the roles/permissions of this user as {@link GrantedAuthority} objects. Spring
   * Security uses this list for @PreAuthorize checks and .hasRole() in SecurityFilterChain.
   *
   * <p>We wrap our Role enum into SimpleGrantedAuthority. The "ROLE_" prefix is required when using
   * hasRole() — Spring automatically prepends it when you call .hasRole("ADMIN").
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
  }

  /**
   * METHOD 2: getPassword()
   *
   * <p>Returns the hashed password. Spring's DaoAuthenticationProvider calls this and then uses a
   * PasswordEncoder (e.g., BCryptPasswordEncoder) to compare it with the raw input.
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * METHOD 3: getUsername()
   *
   * <p>Returns the unique login identifier (email or username). Spring Security uses this as the
   * "principal name" — it appears in SecurityContextHolder after login.
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * METHOD 4: isAccountNonExpired()
   *
   * <p>Returns true → account is still valid (not past expiry date). Returns false → Spring throws
   * AccountExpiredException during authentication.
   */
  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  /**
   * METHOD 5: isAccountNonLocked()
   *
   * <p>Returns true → account is not locked. Returns false → Spring throws LockedException during
   * authentication. Useful for locking accounts after N failed login attempts.
   */
  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  /**
   * METHOD 6: isCredentialsNonExpired()
   *
   * <p>Returns true → password is still valid (not expired). Returns false → Spring throws
   * CredentialsExpiredException. Useful for forcing periodic password resets.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  /**
   * METHOD 7: isEnabled()
   *
   * <p>Returns true → user is active and allowed to log in. Returns false → Spring throws
   * DisabledException. Use this for soft deletes, email verification pending, or manual bans.
   */
  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
