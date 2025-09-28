package com.fdsystem.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordHashingTest {
  @Test
  void testPasswordHashingAndValidation() {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    String rawPassword = "password";
    String hashedPassword = encoder.encode(rawPassword);

    assertNotEquals(rawPassword, hashedPassword);
    assertTrue(encoder.matches(rawPassword, hashedPassword));
    assertFalse(encoder.matches("wrongPass", hashedPassword));
  }
}
