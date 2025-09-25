package com.fdsystem.backend.controller;

import com.fdsystem.backend.model.FixedDeposit;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.util.enums.FdStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private FixedDepositService fixedDepositService;

  public AdminController(FixedDepositService fixedDepositService){
    this.fixedDepositService = fixedDepositService;
  }

  @GetMapping("/fds")
  public ResponseEntity<List<FixedDeposit>> getFds(){
    return new ResponseEntity<>(this.fixedDepositService.getAllFDsByStatus(FdStatus.PENDING), HttpStatus.FOUND);
  }

  @PostMapping("/fd/{id}")
  public ResponseEntity<Void> setFDStatusById(@PathVariable Long id,String status){
    this.fixedDepositService.setFixedDepositStatus(id,FdStatus.valueOf(status));
    return new ResponseEntity<>(HttpStatus.OK);
  }


}
