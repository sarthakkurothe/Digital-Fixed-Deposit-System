package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.LoginRequest;


import com.fdsystem.backend.dto.UserDTO;
import com.fdsystem.backend.model.User;
import com.fdsystem.backend.model.UserPrincipal;
import com.fdsystem.backend.service.UserService;
import com.fdsystem.backend.util.enums.Role;
import com.fdsystem.backend.util.jwt.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    private AuthenticationManager authenticationManager;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
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

        String jwtToken = this.jwtUtils.generateTokenFromUsername(userPrincipal);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if(userService.isUserExists(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
        String hashed_password = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed_password);
        user.setRole(Role.USER);
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        userService.addUser(user);

        return new ResponseEntity<>("User added successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();

        UserDTO userDTO = new UserDTO();
        userDTO.setRole(user.getRole());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setEmail(user.getEmail());

        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }
}
