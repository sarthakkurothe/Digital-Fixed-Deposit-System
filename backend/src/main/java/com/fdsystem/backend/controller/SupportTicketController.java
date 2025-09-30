package com.fdsystem.backend.controller;


import com.fdsystem.backend.dto.TicketRequestDTO;
import com.fdsystem.backend.dto.TicketResponseDTO;
import com.fdsystem.backend.service.SupportTicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support")
public class SupportTicketController {
    private SupportTicketService supportTicketService;

    public SupportTicketController(SupportTicketService supportTicketService){
        this.supportTicketService = supportTicketService;
    }

    /**
     * Creates a new support ticket for a user
     * 
     * @param ticketRequestDTO Contains details of the support ticket to be created
     * @return ResponseEntity with CREATED status if successful
     */
    @PostMapping
    public ResponseEntity<Void> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO){
        this.supportTicketService.createTicket(ticketRequestDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponseDTO>> getSupportTicketsByUserId(@PathVariable long userId){
        return new ResponseEntity<>(this.supportTicketService.getTicketsByUserId(userId),HttpStatus.ACCEPTED);
    }
}
