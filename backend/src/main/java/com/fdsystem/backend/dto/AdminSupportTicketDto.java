package com.fdsystem.backend.dto;

import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import lombok.Data;

import java.util.Date;

@Data
public class AdminSupportTicketDto {
  private long id;
  private String subject;
  private String description;
  private String status;
  private Date createdDate;
  private String name;
  private String email;
}
