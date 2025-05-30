package com.peswoc.hookclient.dto.response.group;

import com.peswoc.hookclient.model.group.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupListResponseDto {
  private List<GroupResponseDto> groups;

  public GroupListResponseDto(List<Group> groups) {
    this.groups = groups.stream()
      .map(GroupResponseDto::new)
      .toList();
  }
}

