package com.fdsystem.backend.dto;

import com.fdsystem.backend.entity.enums.Role;
import lombok.Data;

import java.util.Date;


@Data
public class UserDTO {
  private long id;
  private String name;
  private String email;
  private int age;
  private Role role;
}
