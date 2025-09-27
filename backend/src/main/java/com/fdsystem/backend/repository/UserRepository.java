package com.fdsystem.backend.repository;


import com.fdsystem.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Queue;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  public User findByEmail(String email);

  @Query(value = "select count(*) from users;", nativeQuery = true)
  public Long findAllUserCount();
}

