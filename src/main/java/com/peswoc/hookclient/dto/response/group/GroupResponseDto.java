package com.peswoc.hookclient.dto.response.group;

import com.peswoc.hookclient.model.group.Group;
import com.peswoc.hookclient.model.group.GroupUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupResponseDto {
  private String id;
  private String name;
  private String thumbnail;
  private List<GroupUserResponseDto> users;

  public GroupResponseDto(Group group) {
    this.id = group.getId();
    this.name = group.getName();
    this.thumbnail = group.getThumbnail();
    this.users = null;
  }

  public GroupResponseDto(String id, String name, String thumbnail, List<GroupUser> users) {
    this.id = id;
    this.name = name;
    this.thumbnail = thumbnail;
    this.users = users.stream()
      .map(GroupUserResponseDto::new)
      .toList();
  }
}
