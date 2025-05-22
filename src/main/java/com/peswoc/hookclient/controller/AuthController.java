package com.peswoc.hookclient.controller;

import com.peswoc.hookclient.constant.MessageConst;
import com.peswoc.hookclient.dto.request.auth.ChangePasswordRequestDto;
import com.peswoc.hookclient.dto.request.auth.LoginRequestDto;
import com.peswoc.hookclient.dto.request.auth.RegisterRequestDto;
import com.peswoc.hookclient.service.IAuthService;
import com.peswoc.hookclient.service.IUserService;
import com.peswoc.hookclient.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  IAuthService authService;
  IUserService userService;

  public AuthController(IAuthService authService, IUserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
    if (!authService.validateRegisterFields(request)) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.MISSING_REQUIRED_FIELD);
    } else if (!authService.validateRegisterFormat(request)) {
      return ResponseBuilder.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), MessageConst.INVALID_INPUT_FORMAT);
    } else if (userService.isUsernameExist(request.getUsername()) || userService.isEmailExist(request.getEmail())) {
      return ResponseBuilder.error(HttpStatus.CONFLICT.value(), MessageConst.EMAIL_OR_USERNAME_EXISTS);
    } else {
      return ResponseBuilder.build(HttpStatus.CREATED.value(), MessageConst.REGISTER_SUCCESS, authService.register(request));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
    if (!authService.validateLoginFields(request)) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.MISSING_REQUIRED_FIELD);
    } else if (!authService.validateLoginFormat(request)) {
      return ResponseBuilder.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), MessageConst.INVALID_INPUT_FORMAT);
    } else {
      var user = userService.getUserByEmail(request.getEmail());
      if (user == null) {
        return ResponseBuilder.error(HttpStatus.UNAUTHORIZED.value(), MessageConst.INVALID_EMAIL_OR_PASSWORD);
      } else {
        var username = user.getUsername();
        try {
          return ResponseBuilder.success(authService.login(username, request.getPassword()));
        } catch (BadCredentialsException e) {
          return ResponseBuilder.error(HttpStatus.UNAUTHORIZED.value(), MessageConst.INVALID_EMAIL_OR_PASSWORD);
        }
      }
    }
  }

  @PutMapping("/change-password")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto request) {
    var oldPassword = request.getOldPassword();
    var newPassword = request.getNewPassword();
    if (!authService.validateChangePasswordFields(request)) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.MISSING_REQUIRED_FIELD);
    } else if (!authService.validateChangePasswordFormat(request)) {
      return ResponseBuilder.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), MessageConst.INVALID_INPUT_FORMAT);
    } else if (!authService.isTruePassword(oldPassword)) {
      return ResponseBuilder.error(HttpStatus.UNAUTHORIZED.value(), MessageConst.INVALID_OLD_PASSWORD);
    } else if (newPassword.equals(oldPassword)) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.NEW_PASSWORD_SAME_AS_OLD);
    } else {
      authService.changePassword(request.getNewPassword());
      return ResponseBuilder.build(HttpStatus.OK.value(), MessageConst.PASSWORD_CHANGED_SUCCESS, null);
    }
  }
}
