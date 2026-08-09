package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.model.RefreshToken;
import com.naveen.WorkForceMgmt.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  @Modifying
  void deleteByUser(User user);

  @Modifying
  void deleteByUserAndDeviceId(User user, String deviceId);

  @Modifying
  void deleteByToken(String refreshToken);
}
