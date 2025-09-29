package com.fdsystem.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fdsystem.backend.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long Id;
  private String name;
  @Column(unique = true, nullable = false)
  private String email;
  private String password;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date dateOfBirth;
  @Enumerated(EnumType.STRING)
  private Role role;
  private Timestamp created_at;
}
