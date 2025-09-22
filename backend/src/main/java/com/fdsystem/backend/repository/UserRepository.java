package com.fdsystem.backend.repository;


import com.fdsystem.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  public User findByEmail(String email);
}
