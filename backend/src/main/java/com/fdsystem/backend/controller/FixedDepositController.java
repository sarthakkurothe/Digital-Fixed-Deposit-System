package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.BreakPreviewResponse;
import com.fdsystem.backend.dto.FixedDepositDTO;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.UserService;
import com.fdsystem.backend.entity.enums.FdStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/fd")
public class FixedDepositController {
    @Autowired
    private FixedDepositService fixedDepositService;
    @Autowired
    private UserService userService;

    /**
     * Creates a new fixed deposit for a user
     * 
     * @param fixedDepositDTO Contains details of the fixed deposit to be booked
     * @return ResponseEntity with CREATED status if successful
     */
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

    /**
     * Retrieves all fixed deposits for a specific user
     * 
     * @param userId The ID of the user whose fixed deposits are to be retrieved
     * @return ResponseEntity containing a list of FixedDeposit objects with HTTP status OK
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FixedDeposit>> viewFDs(@PathVariable("userId") long userId){
        List<FixedDeposit> fds = this.fixedDepositService.getFdsByUserId(userId);
        for(FixedDeposit fixedDeposit:fds){
            System.out.println(fixedDeposit.getAmount());
        }
        return new ResponseEntity<>(fds, HttpStatus.OK);
    }

    /**
     * Breaks a fixed deposit before maturity
     * 
     * @param fdId The ID of the fixed deposit to break
     * @return ResponseEntity with CREATED status if successful
     */
    @PostMapping("/{fdId}/break")
    public ResponseEntity<Void> breakFD(@PathVariable Long fdId){
        this.fixedDepositService.breakFD(fdId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Calculates and returns a preview of breaking a fixed deposit
     * 
     * @param fdId The ID of the fixed deposit to get break preview for
     * @return ResponseEntity containing BreakPreviewResponse with break details and HTTP status OK
     */
    @GetMapping("/{fdId}/break-preview")
    public ResponseEntity<BreakPreviewResponse> getPreview(@PathVariable Long fdId){
        BreakPreviewResponse previewResponse = this.fixedDepositService.getBreakPreview(fdId);
        return new ResponseEntity<>(previewResponse, HttpStatus.OK);
    }



    

    /**
     * Retrieves all fixed deposits in the system
     * 
     * @return ResponseEntity containing a list of all FixedDeposit objects with HTTP status OK
     */
    @GetMapping("/fds")
    public ResponseEntity<List<FixedDeposit>> viewAllFDs() {
        List<FixedDeposit> fds = this.fixedDepositService.getAll();
        return new ResponseEntity<>(fds, HttpStatus.OK);
    }


    //GET /fd/{fdId}/interest – View current interest accrued
    /**
     * Retrieves the current accrued interest for a fixed deposit
     * 
     * @param id The ID of the fixed deposit
     * @return ResponseEntity containing the accrued interest value with HTTP status OK
     */
    @GetMapping("/{Id}/interest")
    public ResponseEntity<Double> viewAccruedInterest(@PathVariable("Id") long id){
        Double interest = fixedDepositService.getFdById(id).getAccrued_interest();
        return new ResponseEntity<>(interest, HttpStatus.OK);
    }


    // PUT /fd/{fdId}/status – Mark as matured/broken
    /**
     * Updates the status of a fixed deposit (matured/broken)
     * 
     * @param id The ID of the fixed deposit to update
     * @param status The new status for the fixed deposit
     * @return ResponseEntity with HTTP status OK if successful
     */
    @PutMapping("/{Id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("Id") long id, @RequestParam("status") String status) {
        FixedDeposit fixedDeposit = fixedDepositService.getFdById(id);
        fixedDeposit.setStatus(Enum.valueOf(FdStatus.class, status));
        fixedDepositService.bookFD(fixedDeposit);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
