package com.fdsystem.backend.service;


import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
  }

  @Test
  void testAddUser_Success() {
    when(userRepository.save(user)).thenReturn(user);

    boolean result = userService.addUser(user);

    assertTrue(result);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void testIsUserExists_WhenUserExists() {
    when(userRepository.findByEmail("test@example.com")).thenReturn(user);

    boolean exists = userService.isUserExists("test@example.com");

    assertTrue(exists);
    verify(userRepository, times(1)).findByEmail("test@example.com");
  }

  @Test
  void testIsUserExists_WhenUserDoesNotExist() {
    when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

    boolean exists = userService.isUserExists("notfound@example.com");

    assertFalse(exists);
  }

  @Test
  void testIsUserExists_WhenEmailIsNull() {
    assertThrows(IllegalArgumentException.class, () -> userService.isUserExists(null));
  }

  @Test
  void testGetUserById() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    Optional<User> result = userService.getUserById(1L);

    assertTrue(result.isPresent());
    assertEquals("test@example.com", result.get().getEmail());
  }

  @Test
  void testGetAllUsersCount() {
    when(userRepository.findAllUserCount()).thenReturn(10L);

    Long count = userService.getAllUsersCount();

    assertEquals(10L, count);
    verify(userRepository, times(1)).findAllUserCount();
  }
}
