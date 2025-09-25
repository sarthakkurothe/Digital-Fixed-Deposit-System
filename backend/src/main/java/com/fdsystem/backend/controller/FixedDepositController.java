package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.BreakPreviewResponse;
import com.fdsystem.backend.dto.FixedDepositDTO;
import com.fdsystem.backend.model.FixedDeposit;
import com.fdsystem.backend.model.User;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.UserService;
import com.fdsystem.backend.util.enums.FdStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/fd")
public class FixedDepositController {
    @Autowired
    private FixedDepositService fixedDepositService;
    @Autowired
    private UserService userService;

    @PostMapping("/book")
    public ResponseEntity<Void> bookFD(@RequestBody FixedDepositDTO fixedDepositDTO){
        FixedDeposit fixedDeposit = new FixedDeposit();
        fixedDeposit.setAmount(fixedDepositDTO.getAmount());
        fixedDeposit.setInterest_rate(fixedDepositDTO.getInterest_rate());
        fixedDeposit.setTenure_months(fixedDepositDTO.getTenure_months());

        User user = userService.getUserById(fixedDepositDTO.getUser_id()).get();

        fixedDeposit.setUser(user);

        this.fixedDepositService.bookFD(fixedDeposit);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FixedDeposit>> viewFDs(@PathVariable("userId") long userId){
        List<FixedDeposit> fds = this.fixedDepositService.getFdsByUserId(userId);
        for(FixedDeposit fixedDeposit:fds){
            System.out.println(fixedDeposit.getAmount());
        }
        return new ResponseEntity<>(fds, HttpStatus.OK);
    }

    @PostMapping("/{fdId}/break")
    public ResponseEntity<Void> breakFD(@PathVariable Long fdId){
        this.fixedDepositService.breakFD(fdId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{fdId}/break-preview")
    public ResponseEntity<BreakPreviewResponse> getPreview(@PathVariable Long fdId){
        BreakPreviewResponse previewResponse = this.fixedDepositService.getBreakPreview(fdId);
        return new ResponseEntity<>(previewResponse, HttpStatus.OK);
    }



    

    @GetMapping("/fds")
    public ResponseEntity<List<FixedDeposit>> viewAllFDs() {
        List<FixedDeposit> fds = this.fixedDepositService.getAll();
        return new ResponseEntity<>(fds, HttpStatus.OK);
    }


    //GET /fd/{fdId}/interest – View current interest accrued
    @GetMapping("/{Id}/interest")
    public ResponseEntity<Double> viewAccruedInterest(@PathVariable("Id") long id){
        Double interest = fixedDepositService.getFdById(id).getAccrued_interest();
        return new ResponseEntity<>(interest, HttpStatus.OK);
    }


    // PUT /fd/{fdId}/status – Mark as matured/broken
    @PutMapping("/{Id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("Id") long id, @RequestParam("status") String status) {
        FixedDeposit fixedDeposit = fixedDepositService.getFdById(id);
        fixedDeposit.setStatus(Enum.valueOf(FdStatus.class, status));
        fixedDepositService.bookFD(fixedDeposit);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
