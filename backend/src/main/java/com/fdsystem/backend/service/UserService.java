package com.fdsystem.backend.service;


import com.fdsystem.backend.model.User;
import com.fdsystem.backend.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserService {
  private UserRepository userRepository;
  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  public boolean addUser(User user){
    try {
      userRepository.save(user);
      return true;
    } catch (Exception e) {
      log.error("Error while adding User {}",e.getMessage());
    }
    return false;
  }

  public boolean isUserExists(String email){
    return userRepository.findByEmail(email) != null;
  }


}
