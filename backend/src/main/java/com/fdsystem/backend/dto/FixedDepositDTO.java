package com.fdsystem.backend.dto;


import lombok.Data;

@Data
public class FixedDepositDTO {
    private long user_id;
    private double amount;
    private double interest_rate;
    private int tenure_months;
}
