package com.fdsystem.backend.model;


import com.fdsystem.backend.util.enums.FdStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FixedDeposits")
public class FixedDeposit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;
  private double amount;
  private double interest_rate;
  private int tenure_months;
  private Date start_date;
  private Date maturity_date;
  @Enumerated(EnumType.STRING)
  private FdStatus status;
  private double accrued_interest;
  private Timestamp created_at;

}
