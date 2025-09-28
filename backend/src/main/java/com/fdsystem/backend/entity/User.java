package com.fdsystem.backend.entity;

import com.fdsystem.backend.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
  private int age;
  @Enumerated(EnumType.STRING)
  private Role role;
  private Timestamp created_at;
}
