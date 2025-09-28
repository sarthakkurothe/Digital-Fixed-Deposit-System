package com.fdsystem.backend.service;

import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
    user.setPassword("password"); // needed for authentication
  }

  @Test
  void testLoadUserByUsername_UserExists() {
    when(userRepository.findByEmail("test@example.com")).thenReturn(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

    assertNotNull(userDetails);
    assertEquals("test@example.com", userDetails.getUsername());
    verify(userRepository, times(1)).findByEmail("test@example.com");
  }

  @Test
  void testLoadUserByUsername_UserNotFound() {
    when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

    assertThrows(UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername("notfound@example.com"));

    verify(userRepository, times(1)).findByEmail("notfound@example.com");
  }
}
