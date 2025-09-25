package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.LoginRequest;


import com.fdsystem.backend.dto.UserDTO;
import com.fdsystem.backend.dto.UserDashboardDTO;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.UserPrincipal;
import com.fdsystem.backend.service.AccruedInterestService;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.UserService;
import com.fdsystem.backend.entity.enums.Role;
import com.fdsystem.backend.util.jwt.JWTUtils;
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

    @Autowired
    private FixedDepositService fixedDepositService;
    @Autowired
    private  AccruedInterestService  accruedInterestService;

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
        user.setRole(Role.ROLE_USER);
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        userService.addUser(user);

        return new ResponseEntity<>("User added successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getUser_id());
        userDTO.setRole(user.getRole());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setEmail(user.getEmail());

        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }



    //TotalInvestment , InterestEarned, ActiveFDs,AverageInterest
    @GetMapping("/user/investments")
    public ResponseEntity<?> getDashboardStats(){

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();
        accruedInterestService.calculateAccruedInterest(user);
        long userId = user.getUser_id();

        double totalInvestment = fixedDepositService.getFdsByUserId(userId).stream().mapToDouble(fd -> fd.getAmount()).sum();
        double interestEarned = fixedDepositService.getFdsByUserId(userId).stream().mapToDouble(fd -> fd.getAccrued_interest()).sum();
        long activeFDs = fixedDepositService.getFdsByUserId(userId).stream().filter(fd -> fd.getStatus().toString().equals("ACTIVE")).count();
        double averageInterest = 0.0;
        if(activeFDs > 0){
            averageInterest = interestEarned / activeFDs;
        }

        UserDashboardDTO dashboardDTO = new UserDashboardDTO();
        dashboardDTO.setTotalInvestment(totalInvestment);
        dashboardDTO.setInterestEarned(interestEarned);
        dashboardDTO.setActiveFDs(activeFDs);
        dashboardDTO.setAverageInterest(averageInterest);

        return new ResponseEntity<>(dashboardDTO, HttpStatus.ACCEPTED);
    }


}
