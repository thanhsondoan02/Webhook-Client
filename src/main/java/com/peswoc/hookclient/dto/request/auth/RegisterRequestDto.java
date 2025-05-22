package com.peswoc.hookclient.dto.request.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
  private String username;
  private String email;
  private String password;
  private String fullName;
  private Integer gender;
  private Long dateOfBirth;
}

