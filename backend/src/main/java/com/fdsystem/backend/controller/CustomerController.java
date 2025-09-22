//package com.fdsystem.backend.controller;
//
//import com.fdsystem.backend.dto.LoginRequest;
//
//import com.fdsystem.backend.model.User;
//import com.fdsystem.backend.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class CustomerController {
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//
//    public CustomerController(UserService userService, PasswordEncoder passwordEncoder){
//        this.userService = userService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @GetMapping("/login")
//    public String login(@RequestBody LoginRequest loginRequest) {
//        return null;
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody User user) {
//        if(userService.isCustomerExists(user.getEmail())) {
//            return ResponseEntity.badRequest().body("Email is already taken");
//        }
//
//        userService.addUser(user);
//
//        return ResponseEntity.ok("User registered successfully");
//    }
//}
