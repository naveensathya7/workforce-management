package com.naveen.WorkForceMgmt.service;

import com.naveen.WorkForceMgmt.model.RefreshToken;
import com.naveen.WorkForceMgmt.model.User;
import com.naveen.WorkForceMgmt.repository.RefreshTokenRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.refresh-token.expiration-ms:86400000}")
  private long refreshExpirationMs;

  @Transactional
  public RefreshToken createRefreshToken(User user, String deviceId) {
    refreshTokenRepository.deleteByUserAndDeviceId(user, deviceId);

    refreshTokenRepository.flush();

    RefreshToken refreshToken =
        RefreshToken.builder()
            .user(user)
            .token(UUID.randomUUID().toString())
            .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
            .revoked(false)
            .deviceId(deviceId)
            .build();

    return refreshTokenRepository.save(refreshToken);
  }

  @Transactional
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Transactional
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) <= 0) {
      refreshTokenRepository.delete(token);
      throw new RuntimeException("Refresh token was expired. Please log in again.");
    }
    return token;
  }

  /** Deletes all refresh tokens for a user (useful during logout or password change). */
  @Transactional
  public void deleteByUser(User user) {
    refreshTokenRepository.deleteByUser(user);
  }
}
