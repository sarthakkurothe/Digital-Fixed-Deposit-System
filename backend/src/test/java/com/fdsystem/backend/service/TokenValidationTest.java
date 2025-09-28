package com.fdsystem.backend.service;

import com.fdsystem.backend.util.jwt.AuthTokenFilter;
import com.fdsystem.backend.util.jwt.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenValidationTest {

  @Mock
  private JWTUtils jwtUtils;

  @Mock
  private UserDetailsServiceImpl userDetailsService;

  @InjectMocks
  private AuthTokenFilter authTokenFilter;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void testValidJwtToken_ShouldAuthenticate() throws Exception {
    String jwt = "JWT_VALID_TOKEN";
    String username = "test@gmail.com";

    UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(username)
            .password("password")
            .authorities("ROLE_USER")
            .build();

    when(jwtUtils.getJwtFromHeader(request)).thenReturn(jwt);
    when(jwtUtils.isValidJwtToken(jwt)).thenReturn(true);
    when(jwtUtils.getUsernameFromJwtToken(jwt)).thenReturn(username);
    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

    authTokenFilter.doFilterInternal(request, response, filterChain);

    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    assertEquals(username,
            SecurityContextHolder.getContext().getAuthentication().getName());
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  void testInvalidJwtToken_ShouldNotAuthenticate() throws Exception {
    when(jwtUtils.getJwtFromHeader(request)).thenReturn("INVALID_JWT_TOKEN");
    when(jwtUtils.isValidJwtToken(anyString())).thenReturn(false);

    authTokenFilter.doFilterInternal(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain, times(1)).doFilter(request, response);
  }
}
