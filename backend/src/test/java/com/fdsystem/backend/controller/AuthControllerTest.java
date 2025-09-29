package com.fdsystem.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdsystem.backend.dto.LoginRequest;
import com.fdsystem.backend.dto.UserDTO;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.UserPrincipal;
import com.fdsystem.backend.entity.enums.Role;
import com.fdsystem.backend.service.UserService;
import com.fdsystem.backend.util.jwt.JWTUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtils jwtUtils;

    @Mock(lenient = true)
    private Authentication authentication;

    @Mock(lenient = true)
    private SecurityContext securityContext;

    @InjectMocks
    private AuthController authController;

    // Test data
    private User testUser;
    private LoginRequest loginRequest;
    private UserPrincipal userPrincipal;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up test data
        testUser = createTestUser();
        userPrincipal = new UserPrincipal(testUser);
        loginRequest = createLoginRequest();

        // Set up mock MVC
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        // Set up security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    public void tearDown() {
        // Clear SecurityContextHolder after each test
        SecurityContextHolder.clearContext();
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.ROLE_USER);
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        // Add date of birth to avoid NullPointerException
        user.setDateOfBirth(new java.sql.Date(System.currentTimeMillis() - 25L * 365 * 24 * 60 * 60 * 1000)); // 25 years ago
        return user;
    }

    private LoginRequest createLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        return request;
    }



    @Test
    @DisplayName("Get User Success")
    public void testGetUserSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.name").value(testUser.getName()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.role").value(testUser.getRole().toString()));
    }

    @Test
    @DisplayName("Get User - Direct Controller Method Test")
    public void testGetUserDirectCall() {
        // Given
        // Security context already set up in setUp()

        // When
        ResponseEntity<?> response = authController.getUser();

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserDTO);

        UserDTO returnedUser = (UserDTO) response.getBody();
        assertEquals(testUser.getId(), returnedUser.getId());
        assertEquals(testUser.getName(), returnedUser.getName());
        assertEquals(testUser.getEmail(), returnedUser.getEmail());
        assertEquals(testUser.getRole(), returnedUser.getRole());
    }




}