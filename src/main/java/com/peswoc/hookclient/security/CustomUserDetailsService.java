package com.peswoc.hookclient.security;

import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.model.user.User;
import com.peswoc.hookclient.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsernameAndState(username, State.ACTIVE)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new org.springframework.security.core.userdetails.User(
      user.getUsername(), user.getPasswordHash(), new ArrayList<>());
  }
}
