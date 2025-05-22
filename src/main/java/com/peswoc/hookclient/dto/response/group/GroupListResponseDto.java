package com.peswoc.hookclient.dto.response.group;

import com.peswoc.hookclient.model.group.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupListResponseDto {
  private List<GroupResponseDto> groups;

  public GroupListResponseDto(List<Group> groups) {
    this.groups = groups.stream()
      .map(GroupResponseDto::new)
      .toList();
  }
}

