package com.peswoc.hookclient.dto.request.group;

import com.peswoc.hookclient.constant.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupUserRequestDto {
  private String userId;
  private String role;

  public GroupRole getRole() {
    try {
      return GroupRole.fromString(role);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
