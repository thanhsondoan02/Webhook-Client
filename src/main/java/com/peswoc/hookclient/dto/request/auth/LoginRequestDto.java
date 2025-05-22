package com.peswoc.hookclient.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDto {
  private String email;
  private String password;
}
