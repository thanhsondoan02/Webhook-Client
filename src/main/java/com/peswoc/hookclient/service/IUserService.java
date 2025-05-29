package com.peswoc.hookclient.service;

import com.peswoc.hookclient.dto.response.group.GroupListResponseDto;
import com.peswoc.hookclient.dto.response.user.UserListResponseDto;
import com.peswoc.hookclient.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
  UserListResponseDto getAllUsers();

  UserListResponseDto searchUsers(String key);

  User getUserByUsername(String username);

  User getUserByEmail(String email);

  boolean isUsernameExist(String username);

  boolean isEmailExist(String email);

  boolean isUserIdExist(String id);

  boolean isUserIdExistAndActive(String id);

  void syncUsers(UserListResponseDto data);
}
