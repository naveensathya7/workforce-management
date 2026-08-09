package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.model.RefreshToken;
import com.naveen.WorkForceMgmt.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  void deleteByUser(User user);

  void deleteByUserAndDeviceId(User user, String deviceId);
}
