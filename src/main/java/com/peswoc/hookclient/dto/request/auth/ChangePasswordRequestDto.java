package com.peswoc.hookclient.dto.request.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {
  private String oldPassword;
  private String newPassword;
}
