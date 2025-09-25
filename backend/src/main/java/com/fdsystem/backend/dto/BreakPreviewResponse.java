package com.fdsystem.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreakPreviewResponse {
    private Long fdId;
    private double principalAmount;
    private double accruedInterest;
    private Date startDate;
    private Date maturityDate;
    private double penalty;
    private double payout;
    private double interestRate;
    private double tenure;
    private long timeElapsed;
}
