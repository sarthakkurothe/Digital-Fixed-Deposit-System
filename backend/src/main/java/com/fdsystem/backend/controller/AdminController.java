package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.AdminDashboardDto;
import com.fdsystem.backend.dto.AdminFixedDepositDto;
import com.fdsystem.backend.dto.AdminSupportTicketDto;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.SupportTicketService;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import com.fdsystem.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private FixedDepositService fixedDepositService;
  private SupportTicketService supportTicketService;
  private UserService   userService;

  public AdminController(FixedDepositService fixedDepositService, SupportTicketService supportTicketService, UserService userService){
    this.fixedDepositService = fixedDepositService;
    this.supportTicketService = supportTicketService;
    this.userService = userService;
  }

  @GetMapping("/fds")
  public ResponseEntity<List<AdminFixedDepositDto>> getFds(){
    return new ResponseEntity<>(this.fixedDepositService.getAllFDs(), HttpStatus.OK);
  }

  @PutMapping("/fd/{id}")
  public ResponseEntity<Void> setFDStatusById(@PathVariable Long id, @RequestBody Map<String,String> body) {
    String status = body.get("status");
    if(status == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      FdStatus fdStatus = FdStatus.valueOf(status);
      this.fixedDepositService.setFixedDepositStatus(id, fdStatus);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/tickets")
  public ResponseEntity<List<AdminSupportTicketDto>> getAll(){
    return new ResponseEntity<>(this.supportTicketService.getAll(), HttpStatus.OK);
  }

  @PostMapping("/tickets/{id}")
  public ResponseEntity<Void> setTicketStatusById(@PathVariable long id,@RequestBody String response){
    this.supportTicketService.setTicketStatusById(id, response, SupportTicketStatus.CLOSED);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/dashboard/info")
  public ResponseEntity<AdminDashboardDto> getDashBoardInfo(){
    AdminDashboardDto dashboardDto = new AdminDashboardDto();
    long openCount = this.supportTicketService.getAllStatusTicketsCount(SupportTicketStatus.OPEN.toString());
    long closedCount = this.supportTicketService.getAllStatusTicketsCount(SupportTicketStatus.CLOSED.toString());
    dashboardDto.setTotalOpenTickets(openCount);
    dashboardDto.setTotalTickets(openCount+closedCount);
    dashboardDto.setTotalUsers(this.userService.getAllUsersCount());
    dashboardDto.setTotalFDs(this.fixedDepositService.getAllFdsCount());

    return new ResponseEntity<>(dashboardDto, HttpStatus.OK);

  }
}
