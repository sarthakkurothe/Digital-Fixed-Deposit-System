package com.fdsystem.backend.service;

import com.fdsystem.backend.dto.AdminFixedDepositDto;
import com.fdsystem.backend.dto.AdminSupportTicketDto;
import com.fdsystem.backend.dto.TicketRequestDTO;
import com.fdsystem.backend.dto.TicketResponseDTO;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.SupportTicket;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.SupportTicketRepository;
import com.fdsystem.backend.repository.UserRepository;
import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Service for managing support tickets in the system
 * Handles creation, retrieval, and status updates of support tickets
 */
@Service
public class SupportTicketService {

    private FixedDepositRepository fixedDepositRepository;
    private UserRepository userRepository;
    private SupportTicketRepository supportTicketRepository;

    public SupportTicketService(FixedDepositRepository fixedDepositRepository,UserRepository userRepository, SupportTicketRepository supportTicketRepository){
        this.fixedDepositRepository = fixedDepositRepository;
        this.supportTicketRepository = supportTicketRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new support ticket in the system
     * 
     * @param ticketRequestDTO Contains the details of the support ticket to be created
     *                       including fdId, subject, and description
     */
    public void createTicket(TicketRequestDTO ticketRequestDTO){
        SupportTicket supportTicket = new SupportTicket();
        FixedDeposit fixedDeposit = fixedDepositRepository.findById(ticketRequestDTO.getFdId()).get();
        supportTicket.setFixedDeposit(fixedDeposit);
        supportTicket.setUser(fixedDeposit.getUser());
        supportTicket.setSubject(ticketRequestDTO.getSubject());
        supportTicket.setDescription(ticketRequestDTO.getDescription());
        supportTicket.setStatus(SupportTicketStatus.OPEN);
        supportTicket.setCreatedDate(Date.valueOf(LocalDate.now()));
        supportTicketRepository.save(supportTicket);
    }

    /**
     * Retrieves all support tickets for a specific user
     * 
     * @param userId The ID of the user whose tickets are to be retrieved
     * @return List of TicketResponseDTO objects containing ticket details
     */
    public List<TicketResponseDTO> getTicketsByUserId(long userId){
        User user = this.userRepository.findById(userId).get();
        List<SupportTicket> list = this.supportTicketRepository.findAllByUser(user);
        List<TicketResponseDTO> responseDTOS = new ArrayList<>();
        for(SupportTicket supportTicket: list){
            TicketResponseDTO ticketRequestDTO = new TicketResponseDTO();
            ticketRequestDTO.setStatus(supportTicket.getStatus().toString());
            ticketRequestDTO.setResponse(supportTicket.getResponse());
            ticketRequestDTO.setSubject(supportTicket.getSubject());
            ticketRequestDTO.setDescription(supportTicket.getDescription());
            ticketRequestDTO.setId(supportTicket.getId());
            ticketRequestDTO.setFdId(supportTicket.getFixedDeposit().getId());
            ticketRequestDTO.setCreatedDate(supportTicket.getCreatedDate());
            responseDTOS.add(ticketRequestDTO);
        }

        return responseDTOS;
    }

    /**
     * Retrieves all support tickets in the system for admin view
     * 
     * @return List of AdminSupportTicketDto objects containing ticket details with user information
     */
    public List<AdminSupportTicketDto> getAll(){
        List<SupportTicket> tickets = this.supportTicketRepository.findAll();
        List<AdminSupportTicketDto> ticketDtos = new ArrayList<>();

        for(SupportTicket ticket: tickets){
            AdminSupportTicketDto supportTicketDto = new AdminSupportTicketDto();
            supportTicketDto.setName(ticket.getUser().getName());
            supportTicketDto.setDescription(ticket.getDescription());

            supportTicketDto.setFd(ticket.getFixedDeposit());

            supportTicketDto.setSubject(ticket.getSubject());
            supportTicketDto.setEmail(ticket.getUser().getEmail());
            supportTicketDto.setCreatedDate(ticket.getCreatedDate());
            supportTicketDto.setId(ticket.getId());
            supportTicketDto.setStatus(ticket.getStatus().toString());
            ticketDtos.add(supportTicketDto);
        }

        return ticketDtos;
    }

    /**
     * Updates the status of a support ticket and adds a response
     * 
     * @param id The ID of the ticket to update
     * @param response The response text to add to the ticket
     * @param status The new status for the ticket (e.g., OPEN, CLOSED)
     */
    public void setTicketStatusById(long id,String response, SupportTicketStatus status){
        SupportTicket ticket = this.supportTicketRepository.findById(id).get();
        ticket.setResponse(response);
        ticket.setStatus(status);
        this.supportTicketRepository.save(ticket);
    }

    /**
     * Counts tickets with a specific status
     * 
     * @param status The status to count tickets for (e.g., "OPEN", "CLOSED")
     * @return The count of tickets with the specified status
     */
    public Long getAllStatusTicketsCount(String status){
        return this.supportTicketRepository.getAllStatusTicketsCount(status);
    }
}
