package com.fdsystem.backend.controller;

import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.SupportTicket;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.SupportTicketService;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private FixedDepositService fixedDepositService;
  private SupportTicketService supportTicketService;

  public AdminController(FixedDepositService fixedDepositService, SupportTicketService supportTicketService){
    this.fixedDepositService = fixedDepositService;
    this.supportTicketService = supportTicketService;
  }

  @GetMapping("/fds")
  public ResponseEntity<List<FixedDeposit>> getFds(){
    return new ResponseEntity<>(this.fixedDepositService.getAllFDsByStatus(FdStatus.PENDING), HttpStatus.FOUND);
  }

  @PostMapping("/fd/{id}")
  public ResponseEntity<Void> setFDStatusById(@PathVariable Long id,@RequestBody String status){
    this.fixedDepositService.setFixedDepositStatus(id,FdStatus.valueOf(status));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/tickets")
  public ResponseEntity<List<SupportTicket>> getAllOpenTickets(){
    return new ResponseEntity<>(this.supportTicketService.getAllOpenTickets(), HttpStatus.OK);
  }

  @PostMapping("/tickets/{id}")
  public ResponseEntity<Void> setTicketStatusById(@PathVariable long id,@RequestBody SupportTicketStatus status){
    this.supportTicketService.setTicketStatusById(id, status);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
