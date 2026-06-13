package com.naveen.WorkForceMgmt.repository;

import com.naveen.WorkForceMgmt.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  public Optional<User> findByUsername(String username);
}
