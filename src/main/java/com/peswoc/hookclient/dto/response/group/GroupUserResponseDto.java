package com.peswoc.hookclient.dto.response.group;

import com.peswoc.hookclient.constant.GroupRole;
import com.peswoc.hookclient.model.group.GroupUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserResponseDto {
  private String id;
  private String userId;
  private String role;
  private String username;
  private String fullName;

  public GroupUserResponseDto(GroupUser groupUser) {
    this.userId = groupUser.getUser().getId();
    this.role = groupUser.getRole().name();
    this.username = groupUser.getUser().getUsername();
    this.fullName = groupUser.getUser().getFullName();
  }

  public GroupRole getRole() {
    try {
      return GroupRole.fromString(role);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
