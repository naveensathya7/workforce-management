package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.exception.InvalidRefreshTokenException;
import com.naveen.WorkForceMgmt.model.RefreshToken;
import com.naveen.WorkForceMgmt.model.User;
import com.naveen.WorkForceMgmt.repository.RefreshTokenRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.refresh-token.expiration-ms:86400000}")
  private long refreshExpirationMs;

  @Transactional
  public String createRefreshToken(User user, String deviceId) {
    refreshTokenRepository
        .findByUserAndDeviceIdAndRevokedFalse(user, deviceId)
        .ifPresent(
            existing -> {
              existing.setRevoked((true));
              refreshTokenRepository.save(existing);
            });

    refreshTokenRepository.flush();
    String rawToken = UUID.randomUUID().toString();

    RefreshToken refreshToken =
        RefreshToken.builder()
            .user(user)
            .token(hashToken(rawToken))
            .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
            .revoked(false)
            .deviceId(deviceId)
            .build();

    refreshTokenRepository.save(refreshToken);
    return rawToken;
  }

  @Transactional
  public Optional<RefreshToken> findByToken(String rawToken) {
    return refreshTokenRepository.findByToken(hashToken(rawToken));
  }

  private String hashToken(String rawToken) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 is not available", e);
    }
  }

  @Transactional
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) <= 0) {
      refreshTokenRepository.delete(token);
      throw new InvalidRefreshTokenException("Refresh token was expired. Please log in again.");
    }
    return token;
  }

  /** Deletes all refresh tokens for a user (useful during logout or password change). */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteByUser(User user) {
    refreshTokenRepository.deleteByUser(user);
  }

  @Transactional
  public void deleteByToken(String rawToken) {
    refreshTokenRepository.deleteByToken(hashToken(rawToken));
  }
}
