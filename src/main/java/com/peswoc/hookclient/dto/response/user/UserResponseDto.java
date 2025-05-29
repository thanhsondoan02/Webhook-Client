package com.peswoc.hookclient.dto.response.user;

import com.peswoc.hookclient.constant.Gender;
import com.peswoc.hookclient.constant.Role;
import com.peswoc.hookclient.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
  private String id;
  private String username;
  private String email;
  private String fullName;
  private String gender;
  private String role;
  private Long dateOfBirth;

  public UserResponseDto(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.fullName = user.getFullName();
    if (user.getGender() != null) {
      this.gender = user.getGender().toString();
    }
    if (user.getRole() != null) {
      this.role = user.getRole().toString();
    }
    this.dateOfBirth = user.getDateOfBirth();
  }

  public User toUser() {
    var user = new User();
    user.setId(id);
    user.setUsername(username);
    user.setEmail(email);
    user.setFullName(fullName);
    try {
      user.setGender(Gender.fromString(gender));
    } catch (IllegalArgumentException ignored) {
      user.setGender(null);
    }
    try {
      user.setRole(Role.fromString(role));
    } catch (IllegalArgumentException ignored) {
      user.setRole(null);
    }
    user.setDateOfBirth(dateOfBirth);
    return user;
  }
}

