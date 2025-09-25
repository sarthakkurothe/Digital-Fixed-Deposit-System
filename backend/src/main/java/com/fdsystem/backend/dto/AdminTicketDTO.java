package com.fdsystem.backend.dto;

import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import lombok.Data;

@Data
public class AdminTicketDTO {
  private String response;
  private String status;
}
