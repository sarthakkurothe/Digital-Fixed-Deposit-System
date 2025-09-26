package com.fdsystem.backend.dto;

import lombok.Data;

import java.util.Date;


@Data
public class AdminFixedDepositDto {
  private Long fdId;
  private String name;
  private String email;
  private double amount;
  private double interest_rate;
  private Date mature_date;
  private String fdStatus;
}
