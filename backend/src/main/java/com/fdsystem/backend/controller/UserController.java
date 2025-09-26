package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.UserDashboardDTO;
import com.fdsystem.backend.model.User;
import com.fdsystem.backend.model.UserPrincipal;
import com.fdsystem.backend.service.AccruedInterestService;
import com.fdsystem.backend.service.FixedDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private FixedDepositService fixedDepositService;
    @Autowired
    private AccruedInterestService accruedInterestService;

    //TotalInvestment , InterestEarned, ActiveFDs,AverageInterest
    @GetMapping("/investments")
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
