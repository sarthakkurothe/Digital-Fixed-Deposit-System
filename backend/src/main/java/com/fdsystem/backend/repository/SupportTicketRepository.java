package com.fdsystem.backend.repository;

import com.fdsystem.backend.entity.SupportTicket;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    public List<SupportTicket> findAllByUser(User user);
    public List<SupportTicket> findAllByStatus(SupportTicketStatus status);

    @Query(value = "select count(*) from support_tickets where status = :status", nativeQuery = true)
    public Long  getAllStatusTicketsCount(@Param("status") String status);
}
