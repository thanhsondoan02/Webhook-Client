package com.peswoc.hookclient.service.impl;

import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.dto.response.user.UserListResponseDto;
import com.peswoc.hookclient.dto.response.user.UserResponseDto;
import com.peswoc.hookclient.model.user.User;
import com.peswoc.hookclient.repository.UserRepository;
import com.peswoc.hookclient.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService implements IUserService {
  UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserListResponseDto getAllUsers() {
    return new UserListResponseDto(userRepository.findAllByState(State.ACTIVE));
  }

  @Override
  public UserListResponseDto searchUsers(String key) {
    List<User> users = userRepository.searchByKeyword(key);
    return new UserListResponseDto(users);
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository.findByUsernameAndState(username, State.ACTIVE).orElse(null);
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmailAndState(email, State.ACTIVE).orElse(null);
  }

  @Override
  public boolean isUsernameExist(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public boolean isEmailExist(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public boolean isUserIdExist(String id) {
    return userRepository.existsById(id);
  }

  @Override
  public boolean isUserIdExistAndActive(String id) {
    return userRepository.existsAndActiveById(id);
  }

  @Override
  public void syncUsers(UserListResponseDto data) {
    var users = data.getUsers().stream()
      .map(UserResponseDto::toUser)
      .filter(Objects::nonNull)
      .toList();
    userRepository.saveAll(users);
  }
}
