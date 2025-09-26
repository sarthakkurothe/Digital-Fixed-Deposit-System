package com.fdsystem.backend.service;


import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.UserPrincipal;
import com.fdsystem.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

  private UserRepository userRepository;
  public UserDetailsService(UserRepository userRepository){
    this.userRepository = userRepository;
  }
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findByEmail(username);
    if(user == null){
      log.error("User Not Found");
      throw new UsernameNotFoundException("User Not Exception");
    }
    return new UserPrincipal(user);
  }
}
