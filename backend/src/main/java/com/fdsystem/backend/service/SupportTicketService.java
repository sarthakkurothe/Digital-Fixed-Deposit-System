package com.fdsystem.backend.service;

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

    public List<SupportTicket> getAllOpenTickets(){
        return this.supportTicketRepository.findAllByStatus(SupportTicketStatus.OPEN);
    }

    public void setTicketStatusById(long id,String response, SupportTicketStatus status){
        SupportTicket ticket = this.supportTicketRepository.findById(id).get();
        ticket.setResponse(response);
        ticket.setStatus(status);
        this.supportTicketRepository.save(ticket);
    }
}
