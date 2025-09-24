package com.fdsystem.backend.entity;


import com.fdsystem.backend.util.enums.SupportTicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SupportTickets")
public class SupportTicket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user_id;

  @OneToOne
  @JoinColumn(name = "fd_id")
  private FixedDeposit fd_id;

  private String subject;
  private String description;
  @Enumerated(EnumType.STRING)
  private SupportTicketStatus status;
  private String response;

}
