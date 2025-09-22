package com.fdsystem.backend.model;

import com.fdsystem.backend.util.enums.Role;
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
  private long user_id;
  @Column(unique = true, nullable = false)
  private String email;
  private String password;
  private int age;
  private Role role;
  private Timestamp created_at;
}
