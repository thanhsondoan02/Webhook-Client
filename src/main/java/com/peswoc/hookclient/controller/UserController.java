package com.peswoc.hookclient.controller;

import com.peswoc.hookclient.dto.response.user.UserListResponseDto;
import com.peswoc.hookclient.service.IUserService;
import com.peswoc.hookclient.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final IUserService userService;

  public UserController(IUserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String keyword) {
    UserListResponseDto response;
    if (keyword == null || keyword.isBlank()) {
      response = userService.getAllUsers();
    } else {
      response = userService.searchUsers(keyword);
    }
    return ResponseBuilder.success(response);
  }
}

