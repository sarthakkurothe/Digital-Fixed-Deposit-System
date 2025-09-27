package com.fdsystem.backend.dto;

import lombok.Data;

@Data
public class AdminDashboardDto {
  private long totalUsers;
  private long totalFDs;
  private long totalTickets;
  private long totalOpenTickets;
}
