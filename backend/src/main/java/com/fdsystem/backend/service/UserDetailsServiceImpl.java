package com.fdsystem.backend.service;


import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.UserPrincipal;
import com.fdsystem.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Implementation of Spring Security's UserDetailsService
 * Responsible for loading user-specific data for authentication
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private UserRepository userRepository;
  public UserDetailsServiceImpl(UserRepository userRepository){
    this.userRepository = userRepository;
  }
  /**
   * Loads a user by their email address (used as username)
   * 
   * @param username The email address of the user to load
   * @return UserDetails implementation (UserPrincipal) containing user information
   * @throws UsernameNotFoundException If no user is found with the given email
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findByEmail(username);
    if(user == null){
      log.error("User Not Found");
      throw new UsernameNotFoundException("User Not Found");
    }
    return new UserPrincipal(user);
  }
}
