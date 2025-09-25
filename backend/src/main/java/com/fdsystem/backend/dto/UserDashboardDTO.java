package com.fdsystem.backend.dto;

import lombok.Data;

@Data
public class UserDashboardDTO {
    private Double TotalInvestment;
    private Double InterestEarned;
    private Long ActiveFDs;
    private Double AverageInterest;
}
