package com.fdsystem.backend.model;


import com.fdsystem.backend.util.enums.SupportTicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SupportTickets")
public class SupportTicket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "fd_id")
  private FixedDeposit fixedDeposit;

  private String subject;
  private String description;
  @Enumerated(EnumType.STRING)
  private SupportTicketStatus status;
  private String response;
  private Date createdDate;

}
