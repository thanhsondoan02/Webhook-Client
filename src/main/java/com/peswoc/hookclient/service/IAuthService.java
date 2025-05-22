package com.peswoc.hookclient.service;

import com.peswoc.hookclient.dto.request.auth.ChangePasswordRequestDto;
import com.peswoc.hookclient.dto.request.auth.LoginRequestDto;
import com.peswoc.hookclient.dto.request.auth.RegisterRequestDto;
import com.peswoc.hookclient.dto.response.auth.JwtResponseDto;
import com.peswoc.hookclient.model.user.User;

public interface IAuthService {
  JwtResponseDto register(RegisterRequestDto request);

  JwtResponseDto login(String username, String password);

  void changePassword(String newPassword);

  boolean validateRegisterFields(RegisterRequestDto request);

  boolean validateRegisterFormat(RegisterRequestDto request);

  boolean validateLoginFields(LoginRequestDto request);

  boolean validateLoginFormat(LoginRequestDto request);

  boolean validateChangePasswordFields(ChangePasswordRequestDto request);

  boolean validateChangePasswordFormat(ChangePasswordRequestDto request);

  boolean isTruePassword(String password);

  User getAuthUser();
}
