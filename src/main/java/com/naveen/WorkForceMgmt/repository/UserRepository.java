package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  public Optional<User> findByUsername(String username);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE User u SET u.tokenVersion = u.tokenVersion + 1 WHERE u.id = :userId")
  void incrementTokenVersion(@Param("userId") Long userId);
}
