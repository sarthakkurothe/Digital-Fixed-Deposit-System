package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.LoginRequest;


import com.fdsystem.backend.dto.RefreshRequest;
import com.fdsystem.backend.dto.UserDTO;
import com.fdsystem.backend.dto.UserDashboardDTO;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.UserPrincipal;
import com.fdsystem.backend.exception.TokenExpiredException;
import com.fdsystem.backend.service.AccruedInterestService;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.UserService;
import com.fdsystem.backend.entity.enums.Role;
import com.fdsystem.backend.util.jwt.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;


// rename this file to AuthController

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates a user and generates JWT tokens
     * 
     * @param loginRequest Contains user credentials (email and password)
     * @return ResponseEntity with access and refresh tokens if authentication is successful,
     *         or error details if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        }catch(AuthenticationException e){
            Map<String, Object> map = new HashMap<>();
            map.put("error",e.getMessage());
            map.put("status", false);

            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String refreshToken = this.jwtUtils.generateRefreshToken((userPrincipal.getUsername()));
        String accessToken = this.jwtUtils.generateAccessToken(userPrincipal.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("refreshToken", refreshToken);
        response.put("accessToken", accessToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Registers a new user in the system
     * 
     * @param user User object containing registration details
     * @return ResponseEntity with success message and CREATED status if registration is successful,
     *         or message indicating user already exists
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if(userService.isUserExists(user.getEmail())) {
            return ResponseEntity.ok("User already exists!");
        }
        String hashed_password = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed_password);
        user.setRole(Role.ROLE_USER);
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        userService.addUser(user);

        return new ResponseEntity<>("User added successfully!", HttpStatus.CREATED);
    }

    /**
     * Retrieves the currently authenticated user's information
     * 
     * @return ResponseEntity containing UserDTO with user details and ACCEPTED status
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUser(){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setRole(user.getRole());
        userDTO.setName(user.getName());
        int age = Period.between(user.getDateOfBirth().toLocalDate(), LocalDate.now()).getYears();
        userDTO.setAge(age);
        userDTO.setEmail(user.getEmail());

        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }

    /**
     * Refreshes the user's access token using a valid refresh token
     * 
     * @param refreshRequest Contains the refresh token
     * @return ResponseEntity with a new access token
     * @throws TokenExpiredException If the refresh token is expired or invalid
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody RefreshRequest refreshRequest) throws TokenExpiredException {
        if (!jwtUtils.isValidJwtToken(refreshRequest.getRefreshToken())) {
            throw new TokenExpiredException("Refresh Token got expired!");
        }

        String username = jwtUtils.getUsernameFromJwtToken(refreshRequest.getRefreshToken());
        String newAccessToken = jwtUtils.generateAccessToken(username);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", newAccessToken);

        return ResponseEntity.ok(map);
    }



}