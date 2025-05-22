package com.peswoc.hookclient.dto.response.user;

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
  private Long dateOfBirth;

  public UserResponseDto(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.fullName = user.getFullName();
    if (user.getGender() != null) {
      this.gender = user.getGender().name();
    }
    this.dateOfBirth = user.getDateOfBirth();
  }
}

